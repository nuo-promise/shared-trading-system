package cn.suparking.customer.controller.cargroup.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.common.api.configuration.SnowflakeConfig;
import cn.suparking.common.api.exception.SpkCommonException;
import cn.suparking.common.api.utils.DateUtils;
import cn.suparking.common.api.utils.HttpRequestUtils;
import cn.suparking.common.api.utils.RandomCharUtils;
import cn.suparking.common.api.utils.SpkCommonResultMessage;
import cn.suparking.customer.api.beans.cargroup.CarGroupDTO;
import cn.suparking.customer.api.beans.cargrouporder.CarGroupOrderDTO;
import cn.suparking.customer.api.beans.cargroupstock.CarGroupStockQueryDTO;
import cn.suparking.customer.api.beans.order.OrderDTO;
import cn.suparking.customer.api.beans.vip.VipOrderQueryDTO;
import cn.suparking.customer.api.beans.vip.VipPayDTO;
import cn.suparking.customer.api.constant.ParkConstant;
import cn.suparking.customer.configuration.properties.MiniProperties;
import cn.suparking.customer.configuration.properties.SharedProperties;
import cn.suparking.customer.configuration.properties.SparkProperties;
import cn.suparking.customer.controller.cargroup.service.CarGroupService;
import cn.suparking.customer.controller.cargroup.service.MyVipCarService;
import cn.suparking.customer.controller.cargroup.service.VipOrderQueryService;
import cn.suparking.customer.controller.cargrouporder.service.CarGroupOrderService;
import cn.suparking.customer.controller.cargroupstock.service.CarGroupStockService;
import cn.suparking.customer.dao.entity.CarGroupStockDO;
import cn.suparking.customer.dao.vo.cargroup.MyVipCarVo;
import cn.suparking.customer.dao.vo.cargroup.ProjectVipCarVo;
import cn.suparking.customer.dao.vo.cargroup.ProtocolVipCarVo;
import cn.suparking.customer.spring.SharedTradCustomerInit;
import cn.suparking.customer.tools.OrderUtils;
import cn.suparking.customer.tools.ReactiveRedisUtils;
import cn.suparking.customer.vo.park.MiniPayVO;
import cn.suparking.order.dao.entity.CarGroupOrderDO;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suparking.payutils.controller.ShuBoPaymentUtils;
import com.suparking.payutils.model.APICloseModel;
import com.suparking.payutils.model.APIOrderModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import static cn.suparking.customer.api.constant.ParkConstant.ORDER_TYPE;
import static cn.suparking.customer.api.constant.ParkConstant.PAY_TERM_NO;
import static cn.suparking.customer.api.constant.ParkConstant.PAY_TYPE;
import static cn.suparking.customer.api.constant.ParkConstant.WETCHATMINI;

@Slf4j
@Service("MyVipCarService")
public class MyVipCarServiceImpl implements MyVipCarService {

    private final CarGroupStockService carGroupStockService;

    private final CarGroupService carGroupService;

    private final CarGroupOrderService carGroupOrderService;

    @Resource
    private SharedProperties sharedProperties;

    @Resource
    private SparkProperties sparkProperties;

    @Resource
    private MiniProperties miniProperties;

    public MyVipCarServiceImpl(final CarGroupService carGroupService, final CarGroupOrderService carGroupOrderService,
                               final CarGroupStockService carGroupStockService) {
        this.carGroupService = carGroupService;
        this.carGroupOrderService = carGroupOrderService;
        this.carGroupStockService = carGroupStockService;
    }

    /**
     * 获取当前用户所有场库所办的合约信息.
     *
     * @param sign   秘钥
     * @param userId 用户id
     * @return {@link SpkCommonResult}
     * @author ZDD
     * @date 2022/7/20 14:53:11
     */
    @Override
    public SpkCommonResult myVipCarList(final String sign, final String userId) {
        if (!invoke(sign, userId)) {
            return SpkCommonResult.error(SpkCommonResultMessage.SIGN_NOT_VALID);
        }

        Map<String, Object> params = new HashMap<>();
        params.put("userId", Long.valueOf(userId));
        JSONObject result = HttpRequestUtils.sendGet(sparkProperties.getUrl() + ParkConstant.INTERFACE_MYCARGROUPBYUSERID, params);
        if (Objects.isNull(result) || !ParkConstant.SUCCESS.equals(result.getString("code"))) {
            log.warn("获取用户合约列表失败 <====== 请求失败");
            return SpkCommonResult.error(SpkCommonResultMessage.DETAIL_SUCCESS);
        }

        String myVipCarVoListStr = result.getString("myVipCarVoList");
        List<MyVipCarVo> myVipCarVoList = JSONArray.parseArray(myVipCarVoListStr, MyVipCarVo.class);
        for (MyVipCarVo myVipCarVo : myVipCarVoList) {
            //可以线上续费
            if (myVipCarVo.getCanRenew()) {
                //查询合约库存
                CarGroupStockDO carGroupStock = carGroupStockService.findByProtocolId(myVipCarVo.getProtocolId());
                if (Objects.nonNull(carGroupStock)) {
                    myVipCarVo.setStockId(carGroupStock.getId().toString());
                    // 库存校验 存在 则减去
                    AtomicReference<Integer> tmpQuantity = new AtomicReference<>(0);
                    List<String> keys = getStockGroupKeys(carGroupStock.getId() + "*");
                    if (!keys.isEmpty()) {
                        keys.forEach(key -> {
                            tmpQuantity.updateAndGet(v -> v + getStockGroupQuantity(key));
                        });
                    }
                    myVipCarVo.setStockQuantity(carGroupStock.getStockQuantity() - tmpQuantity.get());
                }
            }
        }
        return SpkCommonResult.success(myVipCarVoList);
    }

    /**
     * 获取线上所有可办合约场库列表.
     *
     * @return {@link SpkCommonResult}
     * @author ZDD
     * @date 2022/7/20 14:53:11
     */
    @Override
    public SpkCommonResult projectVipCarList(final String sign, final String projectNoParams) {
        if (!invoke(sign, projectNoParams)) {
            return SpkCommonResult.error(SpkCommonResultMessage.SIGN_NOT_VALID);
        }
        List<ProjectVipCarVo> projectVipCarVoList = new ArrayList<>();

        //获取所有可线上办理的合约协议
        JSONObject protocolListResult = HttpRequestUtils.sendGet(sparkProperties.getUrl() + ParkConstant.INTERFACE_NEWPROTOCOL, new HashMap<>());
        if (Objects.isNull(protocolListResult) || !ParkConstant.SUCCESS.equals(protocolListResult.getString("code"))) {
            log.warn("获取协议列表失败");
            throw new SpkCommonException("获取场库列表失败");
        }

        JSONArray protocolList = protocolListResult.getJSONArray("protocolList");
        if (Objects.isNull(protocolList)) {
            return SpkCommonResult.success(projectVipCarVoList);
        }

        //取涵盖的场库去重
        Set<String> projectNoList = new HashSet<>();
        for (int i = 0; i < protocolList.size(); i++) {
            JSONObject protocol = protocolList.getJSONObject(i);
            String projectNo = protocol.getString("projectNo");
            projectNoList.add(projectNo);
        }

        //查询场库信息
        Map<String, Object> params = new HashMap<>();
        params.put("projectNoList", new ArrayList<>(projectNoList));
        JSONObject resultJSON = HttpRequestUtils.sendPost(sparkProperties.getUrl() + ParkConstant.INTERFACE_PROJECTLIST, params);
        if (Objects.isNull(resultJSON) || !ParkConstant.SUCCESS.equals(resultJSON.getString("code"))) {
            log.warn("获取场库列表失败");
            throw new SpkCommonException("获取场库列表失败");
        }

        JSONArray projectList = resultJSON.getJSONArray("projectList");
        if (Objects.isNull(projectList)) {
            return SpkCommonResult.success(projectVipCarVoList);
        }

        for (int i = 0; i < projectList.size(); i++) {
            JSONObject project = projectList.getJSONObject(i);
            ProjectVipCarVo projectVipCarVo = ProjectVipCarVo.builder().id(project.getString("id"))
                    .projectNo(project.getString("projectNo"))
                    .projectName(project.getString("projectName"))
                    .address(project.getString("addressSelect"))
                    .status("OPENING")
                    .build();

            JSONObject location = project.getJSONObject("location");
            if (Objects.nonNull(location)) {
                projectVipCarVo.setLongitude(location.getBigDecimal("longitude"));
                projectVipCarVo.setLatitude(location.getBigDecimal("latitude"));
            }
            if (Objects.nonNull(project.getJSONArray("openTime"))) {
                List<String> openTime = JSONArray.parseArray(JSONObject.toJSONString(project.getJSONArray("openTime")), String.class);
                projectVipCarVo.setOpenTime(openTime);
                projectVipCarVo.setStatus(openState(openTime) ? "OPENING" : "CLOSED");
            }
            projectVipCarVoList.add(projectVipCarVo);
        }
        return SpkCommonResult.success(projectVipCarVoList);
    }

    /**
     * 获取线上所有可办合约列表.
     *
     * @param projectNo 场库编号
     * @return {@link ProtocolVipCarVo}
     * @author ZDD
     * @date 2022/7/20 14:53:11
     */
    @Override
    public SpkCommonResult protocolVipCarList(final String sign, final String projectNo) {
        if (!invoke(sign, projectNo)) {
            return SpkCommonResult.error(SpkCommonResultMessage.SIGN_NOT_VALID);
        }

        List<ProtocolVipCarVo> protocolVipCarVoList = new ArrayList<>();

        //获取指定场库可线上办理的合约协议
        Map<String, Object> params = new HashMap<>();
        params.put("projectNo", projectNo);
        JSONObject protocolListResult = HttpRequestUtils.sendGet(sparkProperties.getUrl() + ParkConstant.INTERFACE_NEWPROTOCOLBYPROJECTNO, params);
        if (Objects.isNull(protocolListResult) || !ParkConstant.SUCCESS.equals(protocolListResult.getString("code"))) {
            log.warn("获取协议列表失败");
            throw new SpkCommonException("获取协议列表失败");
        }

        JSONArray protocolList = protocolListResult.getJSONArray("protocolList");
        if (Objects.isNull(protocolList)) {
            return SpkCommonResult.success(protocolVipCarVoList);
        }

        for (int i = 0; i < protocolList.size(); i++) {
            JSONObject protocol = protocolList.getJSONObject(i);
            ProtocolVipCarVo protocolVipCarVo = ProtocolVipCarVo.builder().id(protocol.getString("id"))
                    .projectNo(protocol.getString("projectNo"))
                    .carTypeName(protocol.getString("carTypeName"))
                    .protocolId(protocol.getString("id"))
                    .protocolName(protocol.getString("protocolName"))
                    .protocolDesc(protocol.getString("protocolDesc"))
                    .price(protocol.getInteger("price"))
                    .stockQuantity(0)
                    .build();

            //查询合约库存
            CarGroupStockDO carGroupStock = carGroupStockService.findByProtocolId(protocol.getString("id"));
            if (Objects.nonNull(carGroupStock)) {
                protocolVipCarVo.setStockId(carGroupStock.getId().toString());
                // 库存校验 存在 则减去
                AtomicReference<Integer> tmpQuantity = new AtomicReference<>(0);
                List<String> keys = getStockGroupKeys(carGroupStock.getId() + "*");
                if (!keys.isEmpty()) {
                    keys.forEach(key -> {
                        tmpQuantity.updateAndGet(v -> v + getStockGroupQuantity(key));
                    });
                }
                protocolVipCarVo.setStockQuantity(carGroupStock.getStockQuantity() - tmpQuantity.get());
            }

            JSONObject duration = protocol.getJSONObject("duration");
            if (Objects.nonNull(duration)) {
                protocolVipCarVo.setDurationType(duration.getString("durationType"));
                protocolVipCarVo.setQuantity(duration.getInteger("quantity"));
            }

            protocolVipCarVoList.add(protocolVipCarVo);
        }
        return SpkCommonResult.success(protocolVipCarVoList);
    }

    @Override
    public SpkCommonResult carGroupToPay(final String sign, final VipPayDTO vipPayDTO) {
        // 校验 sign
        if (!invoke(sign, vipPayDTO.getStockId())) {
            return SpkCommonResult.error(SpkCommonResultMessage.SIGN_NOT_VALID);
        }

        String operateType = vipPayDTO.getOperateType();
        //根据用户 合约 校验是否可以办理,组织合约数据
        if (!ParkConstant.RENEW.equals(operateType)) {
            operateType = ParkConstant.NEW;
        }
        CarGroupDTO carGroup = carGroupService.check(vipPayDTO, operateType);
        if (Objects.isNull(carGroup)) {
            return SpkCommonResult.error(SpkCommonResultMessage.CAR_GROUP_DATA_VALID + "下单失败");
        }

        // 下单
        MiniPayVO miniPayVO = MiniPayVO.builder().build();
        // 金额为0,无需走支付 -- 直接发送通知.
        if (vipPayDTO.getDueAmount() == 0) {
            // 走设备服务.
            miniPayVO.setRetCode("0");
            miniPayVO.setNeedQuery(false);
            miniPayVO.setType(ORDER_TYPE);
            String orderNo = OrderUtils.getOrderNo(vipPayDTO.getProjectNo(), String.valueOf(SnowflakeConfig.snowflakeId()));
            miniPayVO.setOutTradeNo(orderNo);

            // TODO 组织数据创建合约 和 创建 合约订单
            vipPayDTO.setOrderNo(orderNo);
            CarGroupOrderDTO carGroupOrder = carGroupOrderService.makeCarGroupOrder(vipPayDTO, carGroup, ParkConstant.SUCCESS, operateType);
            //保存订单
            SpkCommonResult carGroupOrderResult = carGroupOrderService.createOrUpdate(carGroupOrder);
            if (Objects.isNull(carGroupOrderResult) || carGroupOrderResult.getCode() != 200) {
                return SpkCommonResult.error(SpkCommonResultMessage.CAR_GROUP_DATA_VALID + "保存合约订单失败！");
            }
        }

        // 下面进行下单.
        String timeStart = DateUtil.format(new Date(), "yyyyMMddHHmmss");

        // 2分钟后的时间
        Date expireDate = DateUtil.offsetMinute(new Date(), 2);

        // 格式化2分钟之后的时间
        String timeExpire = DateUtil.format(expireDate, "yyyyMMddHHmmss");

        APIOrderModel apiOrderModel = new APIOrderModel();
        apiOrderModel.setProjectNo(vipPayDTO.getProjectNo());
        apiOrderModel.setTermInfo(PAY_TERM_NO);
        apiOrderModel.setTotalAmount(vipPayDTO.getDueAmount());
        apiOrderModel.setTimeStart(timeStart);
        apiOrderModel.setTimeExpire(timeExpire);
        apiOrderModel.setProjectOrderNo(timeStart + ORDER_TYPE);
        apiOrderModel.setNotifyUrl("NO_NOTICE");
        apiOrderModel.setGoodsDesc("合约新办_用户:" + vipPayDTO.getUserId() + ";合约:" + vipPayDTO.getProtocolName()
                + ";有效期:" + vipPayDTO.getBeginDate() + "~" + vipPayDTO.getEndDate());
        apiOrderModel.setGoodsDetail("合约新办_用户:" + vipPayDTO.getUserId() + ";合约:" + vipPayDTO.getProtocolName()
                + ";有效期:" + vipPayDTO.getBeginDate() + "~" + vipPayDTO.getEndDate());
        apiOrderModel.setAttach(vipPayDTO.getProjectName());
        apiOrderModel.setSubject(vipPayDTO.getProjectNo());
        apiOrderModel.setBusinessType("1".charAt(0));
        apiOrderModel.setAppid(miniProperties.getAppid());
        apiOrderModel.setSubopenid(vipPayDTO.getMiniOpenId());
        apiOrderModel.setTradetype(WETCHATMINI);
        log.info("小程序合约下单下单参数 : " + JSON.toJSONString(apiOrderModel));
        String orderResultStr;
        JSONObject retJson;
        try {
            orderResultStr = ShuBoPaymentUtils.order(apiOrderModel);
            retJson = JSON.parseObject(orderResultStr);
        } catch (Exception e) {
            Arrays.stream(e.getStackTrace()).forEach(item -> log.error(item.toString()));
            return SpkCommonResult.error(SpkCommonResultMessage.CAR_GROUP_DATA_VALID + "下单失败");
        }

        // 下单失败,返回结果为空.
        if (Objects.isNull(retJson)) {
            return SpkCommonResult.error(SpkCommonResultMessage.CAR_GROUP_DATA_VALID + "下单失败,返回结果为空");
        }
        String status = retJson.getString("status");
        if ("10008".equals(status)) {
            return SpkCommonResult.error(SpkCommonResultMessage.CAR_GROUP_DATA_VALID + "下单失败,错误码 10008");
        }

        if ("512".equals(status)) {
            log.error("小程序车位锁下单失败,错误码 512,未初始化支付库");
            try {
                SharedTradCustomerInit.initPayTool(RandomCharUtils.getRandomChar(), log);
            } catch (Exception e) {
                Arrays.stream(e.getStackTrace()).forEach(item -> log.error(item.toString()));
            }
            return SpkCommonResult.error(SpkCommonResultMessage.CAR_GROUP_DATA_VALID + "下单失败,错误码 512,未初始化支付库");
        }

        if (!"200".equals(status)) {
            return SpkCommonResult.error(SpkCommonResultMessage.CAR_GROUP_DATA_VALID + "下单失败,错误码 " + status);
        }

        log.info("用户 " + vipPayDTO.getUserId() + " 下单结果返回 : " + retJson);
        if (retJson.getString("status").equals("200")) {
            JSONObject result = retJson.getJSONObject("result");
            String resultCode = result.getString("result_code");
            if ("0".equals(resultCode)) {
                log.info("用户 " + vipPayDTO.getUserId() + " 下单成功,订单号 : " + result.getString("out_trade_no"));
                miniPayVO.setRetCode("0");
                miniPayVO.setNeedQuery(true);
                miniPayVO.setType(ORDER_TYPE);
                miniPayVO.setOutTradeNo(result.getString("out_trade_no"));
                miniPayVO.setPlatForm(result.getString("platform"));
                miniPayVO.setPayInfo(result.getString("payInfo"));

                // TODO 保存合约订单,创建合约
                vipPayDTO.setOrderNo(result.getString("out_trade_no"));
                CarGroupOrderDTO carGroupOrder = carGroupOrderService.makeCarGroupOrder(vipPayDTO, carGroup, ParkConstant.PENDING, operateType);
                SpkCommonResult carGroupOrderResult = carGroupOrderService.createOrUpdate(carGroupOrder);
                if (Objects.isNull(carGroupOrderResult) || carGroupOrderResult.getCode() != 200) {
                    return SpkCommonResult.error(SpkCommonResultMessage.CAR_GROUP_DATA_VALID + "保存合约订单失败！");
                }

                if (OrderUtils.saveOrder(miniPayVO.getOutTradeNo())) {
                    VipOrderQueryDTO vipOrderQueryDTO = VipOrderQueryDTO.builder()
                            .orderNo(miniPayVO.getOutTradeNo())
                            .payType(PAY_TYPE)
                            .termNo(PAY_TERM_NO)
                            .amount(vipPayDTO.getDueAmount())
                            .platForm(miniPayVO.getPlatForm())
                            .vipPayDTO(vipPayDTO)
                            .build();
                    new VipOrderQueryService().queryOrder(vipOrderQueryDTO);
                } else {
                    return SpkCommonResult.error(SpkCommonResultMessage.CAR_GROUP_DATA_VALID + "保存订单到redis失败,无法开启线程查询.");
                }
            } else {
                return SpkCommonResult.error(SpkCommonResultMessage.CAR_GROUP_DATA_VALID + "下单失败,错误信息: " + resultCode + "->" + result.getString("result_desc"));
            }
        }
        return SpkCommonResult.success(miniPayVO);
    }

    @Override
    public SpkCommonResult queryOrder(final String sign, final OrderDTO orderDTO) {
        // 校验 sign
        if (!invoke(sign, orderDTO.getOrderNo())) {
            return SpkCommonResult.error(SpkCommonResultMessage.SIGN_NOT_VALID);
        }
        log.info("小程序合约查询订单参数 : " + JSON.toJSONString(orderDTO) + " 请求时间: " + DateUtil.now());
        JSONObject resultObj = new JSONObject();
        String orderResultStr = OrderUtils.checkOrder(orderDTO.getOrderNo());
        if (StringUtils.isNotBlank(orderResultStr)) {
            if (orderResultStr.equals(orderDTO.getOrderNo())) {
                resultObj.put("query_code", "AA");
            } else {
                JSONObject retObj = JSONObject.parseObject(orderResultStr);
                String code = retObj.getString("code");
                if ("0".equals(code)) {
                    log.info("小程序合约询订单成功,订单号 : " + orderDTO.getOrderNo() + " 时间: " + DateUtil.now());
                    resultObj.put("query_code", "0");
                    if (StringUtils.isNotEmpty(orderDTO.getStockKey()) && deleteStockInfo(orderDTO.getStockKey())) {
                        log.info("库存解除锁定关系成功: " + orderDTO.getStockKey());
                    }
                    if (OrderUtils.deleteOrder(orderDTO.getOrderNo())) {
                        log.info("小程序合约删除订单缓存,成功,订单号 : " + orderDTO.getOrderNo());
                    }
                } else {
                    log.info("小程序合约查询订单失败,订单号 : " + orderDTO.getOrderNo() + " 时间: " + DateUtil.now());
                    resultObj.put("query_code", code);
                    resultObj.put("query_msg", retObj.getString("msg"));
                }

            }
        } else {
            log.warn("Redis 中无订单号 : " + orderDTO.getOrderNo() + " 的订单信息");
            resultObj.put("query_code", "-1");
            resultObj.put("query_msg", "查询失败");
        }
        return SpkCommonResult.success(resultObj);
    }

    @Override
    public SpkCommonResult closeOrder(final String sign, final OrderDTO orderDTO) {
        // 校验 sign
        if (!invoke(sign, orderDTO.getOrderNo())) {
            return SpkCommonResult.error(SpkCommonResultMessage.SIGN_NOT_VALID);
        }
        log.info("小程序合约关闭订单参数 : " + JSON.toJSONString(orderDTO) + " 请求时间: " + DateUtil.now());
        APICloseModel closeModel = new APICloseModel();
        closeModel.setOrderNo(orderDTO.getOrderNo());
        closeModel.setProjectNo(orderDTO.getProjectNo());
        String resultStr = ShuBoPaymentUtils.close(closeModel);
        JSONObject resultObj = JSONObject.parseObject(resultStr);
        JSONObject retObj = new JSONObject();
        if (Objects.isNull(resultObj)) {
            log.info("支付库关单失败,返回为null");
            retObj.put("code", "10002");
            retObj.put("msg", "业务失败");
        }
        log.info("支付库关单返回结果 : " + resultStr);
        String status = resultObj.getString("status");
        if ("200".equals(status)) {
            JSONObject result = resultObj.getJSONObject("result");
            if ("0".equals(result.getString("result_code"))) {
                log.info("支付库关单成功");
                retObj.put("code", "0");
            }
        } else if ("510".equals(status)) {
            log.info("status = 510, 支付渠道不支持操作");
            retObj.put("code", "12022");
            retObj.put("msg", "不支持关单");
        } else {
            log.info("支付库关单失败");
            retObj.put("code", "10002");
            retObj.put("msg", "业务失败");
        }
        OrderUtils.deleteOrder(orderDTO.getOrderNo());
        if (StringUtils.isNotBlank(orderDTO.getStockKey())) {
            deleteStockInfo(orderDTO.getStockKey());
        }
        // //修改订单状态为失败
        vipOrderPayFailed(orderDTO.getOrderNo());
        return SpkCommonResult.success(retObj);
    }

    @Override
    public SpkCommonResult clearStockInfoCache(final String sign, final OrderDTO orderDTO) {
        // 校验 sign
        if (!invoke(sign, orderDTO.getStockKey())) {
            return SpkCommonResult.error(SpkCommonResultMessage.SIGN_NOT_VALID);
        }
        log.info("小程序合约办理清除库存缓存参数 : " + JSON.toJSONString(orderDTO) + " 请求时间: " + DateUtil.now());
        if (deleteStockInfo(orderDTO.getStockKey())) {
            log.info("小程序合约办理清除缓存成功 : " + orderDTO.getStockKey());
            return SpkCommonResult.success("清除缓存成功");
        } else {
            log.info("小程序优惠券清除缓存失败 : " + orderDTO.getStockKey());
            return SpkCommonResult.success("清除缓存失败");
        }
    }


    /**
     * 支付成功修改合约、合约订单状态，库存操作.
     *
     * @param orderNo 订单号
     * @author ZDD
     * @date 2022/7/22 16:29:23
     */
    @Override
    public void vipOrderPaySuccess(final String orderNo, final VipPayDTO vipPayDTO) {
        CarGroupOrderDO carGroupOrderDO = carGroupOrderService.findByOrderNo(orderNo);
        //修改合约订单状态
        CarGroupOrderDTO carGroupOrderDTO = CarGroupOrderDTO.builder().id(String.valueOf(carGroupOrderDO.getId()))
                .userId(carGroupOrderDO.getUserId()).carGroupId(String.valueOf(carGroupOrderDO.getCarGroupId()))
                .orderState("SUCCESS").build();
        carGroupOrderService.createOrUpdate(carGroupOrderDTO);
        //修改合约状态
        if (ParkConstant.RENEW.equals(vipPayDTO.getOperateType())) {
            //续费合约
            CarGroupDTO carGroup = carGroupService.check(vipPayDTO, vipPayDTO.getOperateType());

            Map<String, Object> params = new HashMap<>();
            JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(carGroup));
            for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                params.put(entry.getKey(), entry.getValue());
            }

            HttpRequestUtils.sendPost(sparkProperties.getUrl() + ParkConstant.INTERFACE_RENEW, params);
        } else {
            Map<String, Object> params = new HashMap<>();
            params.put("id", carGroupOrderDO.getCarGroupId());
            HttpRequestUtils.sendGet(sparkProperties.getUrl() + ParkConstant.INTERFACE_MODIFYVALID, params);
        }
        //修改库存
        CarGroupStockQueryDTO carGroupStockQueryDTO = CarGroupStockQueryDTO.builder().operateType(ParkConstant.DECREASE).quantity(vipPayDTO.getQuantity())
                .modifier(ParkConstant.SYSTEM).id(Long.valueOf(vipPayDTO.getStockId())).termNo("501").build();
        carGroupStockService.operate(carGroupStockQueryDTO);
    }

    /**
     * 支付失败修改合约、合约订单状态，库存操作.
     *
     * @param orderNo 订单号
     * @author ZDD
     * @date 2022/7/22 16:29:23
     */
    @Override
    public void vipOrderPayFailed(final String orderNo) {
        CarGroupOrderDO carGroupOrderDO = carGroupOrderService.findByOrderNo(orderNo);
        //修改合约订单状态
        CarGroupOrderDTO carGroupOrderDTO = CarGroupOrderDTO.builder().id(String.valueOf(carGroupOrderDO.getId()))
                .orderState("FAILED").carGroupId(String.valueOf(carGroupOrderDO.getCarGroupId()))
                .userId(carGroupOrderDO.getUserId()).build();
        carGroupOrderService.createOrUpdate(carGroupOrderDTO);
    }

    /**
     * delete stockInfo.
     *
     * @param stockKey the stock
     */
    private Boolean deleteStockInfo(final String stockKey) {
        return ReactiveRedisUtils.deleteValue(stockKey).block(Duration.ofMillis(3000));
    }

    /**
     * 模糊查找某个Key.
     *
     * @param keyPattern String
     * @return {@link List}
     */
    private List<String> getStockGroupKeys(final String keyPattern) {
        return ReactiveRedisUtils.getKeys(keyPattern).collectList().block(Duration.ofMillis(3000));
    }

    /**
     * 根据Key 获取 库存数量.
     *
     * @param key String
     * @return {@link Integer}
     */
    private Integer getStockGroupQuantity(final String key) {
        return (Integer) ReactiveRedisUtils.getData(key).block(Duration.ofMillis(3000));
    }

    /**
     * 开放状态.
     *
     * @param openTime 开放时间
     * @return boolean
     * @author ZDD
     * @date 2022/7/20 20:38:07
     */
    private boolean openState(final List<String> openTime) {
        if (CollectionUtils.isEmpty(openTime) || openTime.size() < 2) {
            return false;
        }

        //当前距离凌晨的秒值
        long currentMillis = 0L;
        long now = System.currentTimeMillis();
        SimpleDateFormat sdfOne = new SimpleDateFormat("yyyy-MM-dd");
        try {
            currentMillis = (now - (sdfOne.parse(sdfOne.format(now)).getTime())) / 1000;
        } catch (ParseException e) {
            log.warn("获取时间数据异常 [{}]", e.getMessage());
            e.printStackTrace();
        }

        String startTime = openTime.get(0);
        String endTime = openTime.get(1);

        //开始时间距离凌晨秒值
        long startSecond = 0L;
        startSecond = startSecond + Integer.parseInt(startTime.split(":")[0]) * 60 * 60;
        startSecond = startSecond + Integer.parseInt(startTime.split(":")[1]) * 60;

        //结束时间距离凌晨秒值
        long endSecond = 0L;
        endSecond = endSecond + Integer.parseInt(endTime.split(":")[0]) * 60 * 60;
        endSecond = endSecond + Integer.parseInt(endTime.split(":")[1]) * 60;

        //跨天处理
        if (startSecond > endSecond) {
            return currentMillis <= startSecond || currentMillis >= endSecond;
        } else {
            return currentMillis >= startSecond && currentMillis <= endSecond;
        }
    }

    /**
     * check sign.
     *
     * @param sign     sign.
     * @param deviceNo deviceNo
     * @return Boolean
     */
    private Boolean invoke(final String sign, final String deviceNo) {
        return md5(sharedProperties.getSecret() + deviceNo + DateUtils.currentDate() + sharedProperties.getSecret(), sign);
    }

    /**
     * MD5.
     *
     * @param data  the data
     * @param token the token
     * @return boolean
     */
    private boolean md5(final String data, final String token) {
        String keyStr = DigestUtils.md5Hex(data.toUpperCase()).toUpperCase();
        log.info("Mini MD5 Value: " + keyStr);
        if (keyStr.equals(token)) {
            return true;
        } else {
            log.warn("Mini Current MD5 :" + keyStr + ", Data Token : " + token);
        }
        return false;
    }
}
