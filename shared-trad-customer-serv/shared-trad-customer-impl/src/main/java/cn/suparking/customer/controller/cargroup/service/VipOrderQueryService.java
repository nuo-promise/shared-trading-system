package cn.suparking.customer.controller.cargroup.service;

import cn.suparking.customer.api.beans.discount.DiscountUsedDTO;
import cn.suparking.customer.api.beans.order.OrderQueryDTO;
import cn.suparking.customer.api.beans.vip.VipOrderQueryDTO;
import cn.suparking.customer.concurrent.SparkingThreadFactory;
import cn.suparking.customer.configuration.properties.SharedProperties;
import cn.suparking.customer.controller.park.service.impl.OrderServiceImpl;
import cn.suparking.customer.tools.BeansManager;
import cn.suparking.customer.tools.ReactiveRedisUtils;
import cn.suparking.data.api.parkfee.DiscountInfo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.suparking.payutils.controller.ShuBoPaymentUtils;
import com.suparking.payutils.model.APIOrderQueryModel;
import com.suparking.payutils.tools.GenericResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class VipOrderQueryService {

    private static final Logger LOG = LoggerFactory.getLogger(VipOrderQueryService.class);

    // 定时器中断线程Map.
    private static final Map<String, ScheduledFuture<?>> FUTUREMAP = new ConcurrentHashMap<>();

    // 线程执行器.
    private static ScheduledThreadPoolExecutor executor;

    private final SharedProperties sharedProperties = BeansManager.getBean("SharedProperties", SharedProperties.class);

    private final OrderServiceImpl orderService = BeansManager.getBean("OrderService", OrderServiceImpl.class);

    // 临停订单查询变量.
    private VipOrderQueryDTO vipOrderQueryDTO;

    private Integer count = 0;

    public VipOrderQueryService() {
        if (Objects.isNull(executor)) {
            executor = new ScheduledThreadPoolExecutor(sharedProperties.getCorePoolSize(), SparkingThreadFactory.create("pay-vip-order-query", true));
        }
    }

    /**
     * 临停单查询.
     * @param vipOrderQueryDTO {@link VipOrderQueryDTO}
     */
    public void queryOrder(final VipOrderQueryDTO vipOrderQueryDTO) {
        this.vipOrderQueryDTO = vipOrderQueryDTO;
        count = 0;
        if (StringUtils.isEmpty(checkOrder(vipOrderQueryDTO.getOrderNo()))) {
            LOG.error("合约办理查询失败,缓存中无对应订单信息，订单号：" + vipOrderQueryDTO.getOrderNo());
            return;
        }

        ScheduledFuture<?> vipOrderQueryFuture = FUTUREMAP.get(vipOrderQueryDTO.getOrderNo());
        if (Objects.nonNull(vipOrderQueryFuture)) {
            LOG.info("合约办理单查询失败,执行器中已存在对应订单查询信息，订单号： " + vipOrderQueryDTO.getOrderNo());
            return;
        }

        vipOrderQueryFuture = executor.scheduleWithFixedDelay(this::orderQuery, 0, sharedProperties.getQueryInterval(), TimeUnit.SECONDS);
        FUTUREMAP.put(vipOrderQueryDTO.getOrderNo(), vipOrderQueryFuture);
        LOG.info("订单号: " + vipOrderQueryDTO.getOrderNo() + " 开始合约办理订单查询,开启成功...");
    }

    /**
     * 临停单查询.
     */
    private void orderQuery() {
        APIOrderQueryModel apiOrderQueryModel = new APIOrderQueryModel();
        apiOrderQueryModel.setProjectNo(vipOrderQueryDTO.getVipPayDTO().getProjectNo());
        apiOrderQueryModel.setOrderNo(vipOrderQueryDTO.getOrderNo());
        LOG.info("小程序合办理约订单号查询参数 : " + JSON.toJSONString(apiOrderQueryModel));
        String orderQueryResultStr = "";

        try {
            orderQueryResultStr = ShuBoPaymentUtils.orderquery(apiOrderQueryModel);
        } catch (Exception e) {
            Arrays.stream(e.getStackTrace()).forEach(item -> LOG.error(item.toString()));
            return;
        }

        if (StringUtils.isBlank(orderQueryResultStr)) {
            LOG.error("临停单查询失败,返回 null,订单号：" + vipOrderQueryDTO.getOrderNo());
            return;
        }

        GenericResponse genericResponse = JSON.parseObject(orderQueryResultStr, GenericResponse.class);
        if (Objects.isNull(genericResponse)) {
            LOG.error("临停单查询失败,返回 null,订单号：" + vipOrderQueryDTO.getOrderNo());
            return;
        }
        LOG.info("订单号: " + vipOrderQueryDTO.getOrderNo() + "查询次数: " + count++ + " 临停单查询返回结果：" + JSON.toJSONString(genericResponse));
        int status = genericResponse.getStatus();
        if (status != 200) {
            LOG.error("临停单查询失败,返回状态码不为200,订单号：" + vipOrderQueryDTO.getOrderNo());
            return;
        }
        Object result = genericResponse.getResult();
        if (Objects.isNull(result)) {
            LOG.error("合约办理订单查询失败,返回结果为null,订单号：" + vipOrderQueryDTO.getOrderNo());
            return;
        }
        JSONObject resultObj = JSON.parseObject(result.toString());
        String code = resultObj.getString("result_code");
        if ("0".equals(code)) {
            LOG.info("订单号: " + vipOrderQueryDTO.getOrderNo() + " 查询支付成功，合约订单查询结束...");
            // 取消定时任务.
            if (vipOrderPaySuccess(vipOrderQueryDTO.getOrderNo())) {
                LOG.info("订单: " + vipOrderQueryDTO.getOrderNo() + " 后续工作处理完成,停止线程...");
            }

            FUTUREMAP.get(vipOrderQueryDTO.getOrderNo()).cancel(true);
            FUTUREMAP.remove(vipOrderQueryDTO.getOrderNo());

        } else if ("AB".equals(code)) {
            LOG.info("订单号: " + vipOrderQueryDTO.getOrderNo() + " 查询支付未成功，合约订单查询结束...");
            // 删除Redis 中订单
            if (deleteOrder(vipOrderQueryDTO.getOrderNo())) {
                LOG.info("订单: " + vipOrderQueryDTO.getOrderNo() + " 清除订单数据完成,停止线程...");
            }
            // 取消定时任务.
            FUTUREMAP.get(vipOrderQueryDTO.getOrderNo()).cancel(true);
            FUTUREMAP.remove(vipOrderQueryDTO.getOrderNo());
        } else {
            LOG.info("订单号: " + vipOrderQueryDTO.getOrderNo() + "查询次数: " + count++ + " 合约办理订单查询返回结果处于支付中...");
        }
    }

    /**
     * 支付成功存入Redis.
     * @param orderNo String
     */
    private Boolean vipOrderPaySuccess(final String orderNo) {
        JSONObject result = new JSONObject();
        result.put("code", "0");
        result.put("msg", "支付完成");
        if (!saveOrder(orderNo, result.toJSONString())) {
            LOG.info("订单号: " + orderNo + " 订单完成, 存入Redis失败...");
        }
        // 创建合约订单和合约办理,更新库存.

        return true;
    }

    /**
     * 检查订单是否存在.
     * @param orderNo String
     * @return {@link Boolean}
     */
    private String checkOrder(final String orderNo) {
        return (String) ReactiveRedisUtils.getData(orderNo).block(Duration.ofMillis(3000));
    }

    /**
     * 将订单信息存入redis.
     * @param orderNo 订单号.
     * @return 执行结果.
     */
    private Boolean saveOrder(final String orderNo, final String result) {
        return ReactiveRedisUtils.putValue(orderNo, result, 3 * 60).block(Duration.ofMillis(3000));
    }

    /**
     * 删除订单redis缓存.
     * @param orderNo String
     * @return Boolean
     */
    private Boolean deleteOrder(final String orderNo) {
        return ReactiveRedisUtils.deleteValue(orderNo).block(Duration.ofMillis(3000));
    }
}
