package cn.suparking.customer.controller.park.service;

import cn.suparking.customer.api.beans.discount.DiscountUsedDTO;
import cn.suparking.customer.api.beans.order.OrderQueryDTO;
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

public class OrderQueryService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderQueryService.class);

    // 定时器中断线程Map.
    private static final Map<String, ScheduledFuture<?>> FUTUREMAP = new ConcurrentHashMap<>();

    // 线程执行器.
    private static ScheduledThreadPoolExecutor executor;

    private final SharedProperties sharedProperties = BeansManager.getBean("SharedProperties", SharedProperties.class);

    private final OrderServiceImpl orderService = BeansManager.getBean("OrderService", OrderServiceImpl.class);

    // 临停订单查询变量.
    private OrderQueryDTO orderQueryDTO;

    private Integer count = 0;

    public OrderQueryService() {
        if (Objects.isNull(executor)) {
            executor = new ScheduledThreadPoolExecutor(sharedProperties.getCorePoolSize(), SparkingThreadFactory.create("pay-order-query", true));
        }
    }

    /**
     * 临停单查询.
     * @param orderQueryDTO {@link OrderQueryDTO}
     */
    public void queryOrder(final OrderQueryDTO orderQueryDTO) {
        this.orderQueryDTO = orderQueryDTO;
        count = 0;
        if (StringUtils.isEmpty(checkOrder(orderQueryDTO.getOrderNo()))) {
            LOG.error("临停单查询失败,缓存中无对应订单信息，订单号：" + orderQueryDTO.getOrderNo());
            return;
        }

        ScheduledFuture<?> orderQueryFuture = FUTUREMAP.get(orderQueryDTO.getOrderNo());
        if (Objects.nonNull(orderQueryFuture)) {
            LOG.info("临停单查询失败,执行器中已存在对应订单查询信息，订单号： " + orderQueryDTO.getOrderNo());
            return;
        }

        orderQueryFuture = executor.scheduleWithFixedDelay(this::orderQuery, 0, sharedProperties.getQueryInterval(), TimeUnit.SECONDS);
        FUTUREMAP.put(orderQueryDTO.getOrderNo(), orderQueryFuture);
        LOG.info("订单号: " + orderQueryDTO.getOrderNo() + " 开始临停单查询,开启成功...");
    }

    /**
     * 临停单查询.
     */
    private void orderQuery() {

        APIOrderQueryModel apiOrderQueryModel = new APIOrderQueryModel();
        apiOrderQueryModel.setProjectNo(orderQueryDTO.getParking().getProjectNo());
        apiOrderQueryModel.setOrderNo(orderQueryDTO.getOrderNo());
        LOG.info("小程序订单号查询参数 : " + JSON.toJSONString(apiOrderQueryModel));
        String orderQueryResultStr = "";

        try {
            orderQueryResultStr = ShuBoPaymentUtils.orderquery(apiOrderQueryModel);
        } catch (Exception e) {
            Arrays.stream(e.getStackTrace()).forEach(item -> LOG.error(item.toString()));
            return;
        }

        if (StringUtils.isBlank(orderQueryResultStr)) {
            LOG.error("临停单查询失败,返回 null,订单号：" + orderQueryDTO.getOrderNo());
            return;
        }

        GenericResponse genericResponse = JSON.parseObject(orderQueryResultStr, GenericResponse.class);
        if (Objects.isNull(genericResponse)) {
            LOG.error("临停单查询失败,返回 null,订单号：" + orderQueryDTO.getOrderNo());
            return;
        }
        LOG.info("订单号: " + orderQueryDTO.getOrderNo() + "查询次数: " + count++ + " 临停单查询返回结果：" + JSON.toJSONString(genericResponse));
        int status = genericResponse.getStatus();
        if (status != 200) {
            LOG.error("临停单查询失败,返回状态码不为200,订单号：" + orderQueryDTO.getOrderNo());
            return;
        }
        Object result = genericResponse.getResult();
        if (Objects.isNull(result)) {
            LOG.error("临停单查询失败,返回结果为null,订单号：" + orderQueryDTO.getOrderNo());
            return;
        }
        JSONObject resultObj = JSON.parseObject(result.toString());
        String code = resultObj.getString("result_code");
        if ("0".equals(code)) {
            LOG.info("订单号: " + orderQueryDTO.getOrderNo() + " 查询支付成功，临停单查询结束...");
            // 取消定时任务.
            if (parkingOrderPaySuccess(orderQueryDTO.getOrderNo())) {
               LOG.info("订单号: " + orderQueryDTO.getOrderNo() + " 后续事情处理完成，临停单查询结束...");
            }
            FUTUREMAP.get(orderQueryDTO.getOrderNo()).cancel(true);
            FUTUREMAP.remove(orderQueryDTO.getOrderNo());
        } else if ("AB".equals(code)) {
            LOG.info("订单号: " + orderQueryDTO.getOrderNo() + " 查询支付未成功，临停单查询结束...");
            // 删除Redis 中订单
            if (deleteOrder(orderQueryDTO.getOrderNo())) {
               LOG.info("订单号: " + orderQueryDTO.getOrderNo() + " 清单清理完成，临停单查询结束...");
            }
            // 取消定时任务.
            FUTUREMAP.get(orderQueryDTO.getOrderNo()).cancel(true);
            FUTUREMAP.remove(orderQueryDTO.getOrderNo());
        } else {
            LOG.info("订单号: " + orderQueryDTO.getOrderNo() + "查询次数: " + count++ + " 临停单查询返回结果处于支付中...");
        }
    }

    /**
     * 支付成功存入Redis.
     * @param orderNo String
     */
    private Boolean parkingOrderPaySuccess(final String orderNo) {
        JSONObject result = new JSONObject();
        result.put("code", "0");
        result.put("msg", "支付完成");
        if (!saveOrder(orderNo, result.toJSONString())) {
            LOG.info("订单号: " + orderNo + " 订单完成, 存入Redis失败...");
        }
        // 通知设备开锁.
        orderQueryDTO.getParkingOrder().setStatus("COMPLETE");
        if (orderService.saveOrder(orderQueryDTO.getParkingOrder(), orderQueryDTO.getParking(), orderQueryDTO.getOrderNo(),
                orderQueryDTO.getPayType(), orderQueryDTO.getTermNo(), orderQueryDTO.getAmount(), orderQueryDTO.getPlatForm())) {
            LOG.info("订单号: " + orderNo + "更新成功, 发送开闸指令");
            if (orderService.openCtpDevice(orderQueryDTO.getParking().getDeviceNo())) {
                LOG.info("用户编号: " + orderQueryDTO.getParking().getUserId() + " 订单号: " + orderNo + "开闸成功");
            } else {
                LOG.info("用户编号: " + orderQueryDTO.getParking().getUserId() + " 订单号: " + orderNo + "开闸失败");
            }
            // 如果存在使用优惠券则进行核销操作.
            DiscountInfo discountInfo = orderQueryDTO.getParkingOrder().getDiscountInfo();
            if (Objects.nonNull(discountInfo)) {
                JSONObject discountInfoObj = new JSONObject();
                discountInfoObj.put("discountNo", discountInfo.getDiscountNo());
                discountInfoObj.put("valueType", discountInfo.getValueType());
                discountInfoObj.put("value", discountInfo.getValue());
                discountInfoObj.put("quantity", discountInfo.getQuantity());
                discountInfoObj.put("usedStartTime", discountInfo.getUsedStartTime());
                discountInfoObj.put("usedEndTime", discountInfo.getUsedEndTime());
                DiscountUsedDTO discountUsedDTO = DiscountUsedDTO.builder()
                        .userId(orderQueryDTO.getParking().getUserId().toString())
                        .orderNo(orderQueryDTO.getParkingOrder().getOrderNo())
                        .projectNo(orderQueryDTO.getParking().getProjectNo())
                        .discountInfo(discountInfoObj)
                        .build();
                if (orderService.discountUsed(discountUsedDTO)) {
                    LOG.info("用户ID: " + orderQueryDTO.getParking().getUserId() + " 订单号: " + orderNo + " 优惠券: " + discountInfo.getDiscountNo() + " 核销成功");
                } else {
                    LOG.info("用户ID: " + orderQueryDTO.getParking().getUserId() + " 订单号: " + orderNo + " 优惠券: " + discountInfo.getDiscountNo() + " 核销失败");
                }
            }
        }
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
