package cn.suparking.customer.controller.park.service.impl;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.common.api.configuration.SnowflakeConfig;
import cn.suparking.common.api.utils.DateUtils;
import cn.suparking.common.api.utils.HttpUtils;
import cn.suparking.common.api.utils.SpkCommonResultMessage;
import cn.suparking.common.api.utils.Utils;
import cn.suparking.customer.api.beans.ParkFeeQueryDTO;
import cn.suparking.customer.api.beans.ParkPayDTO;
import cn.suparking.customer.api.beans.ProjectQueryDTO;
import cn.suparking.customer.api.beans.parkfee.DiscountCustomer;
import cn.suparking.customer.api.beans.parkfee.ParkFeeRet;
import cn.suparking.customer.api.beans.parkfee.Parking;
import cn.suparking.customer.api.beans.parkfee.ParkingOrder;
import cn.suparking.customer.api.constant.ParkConstant;
import cn.suparking.customer.beans.park.LocationDTO;
import cn.suparking.customer.beans.park.RegularLocationDTO;
import cn.suparking.customer.configuration.properties.RabbitmqProperties;
import cn.suparking.customer.configuration.properties.SharedProperties;
import cn.suparking.customer.configuration.properties.SparkProperties;
import cn.suparking.customer.controller.park.service.ParkService;
import cn.suparking.customer.dao.vo.user.ParkFeeQueryVO;
import cn.suparking.customer.feign.data.DataTemplateService;
import cn.suparking.customer.feign.user.UserTemplateService;
import cn.suparking.customer.tools.ReactiveRedisUtils;
import cn.suparking.customer.vo.park.ParkInfoVO;
import cn.suparking.customer.vo.park.ParkPayVO;
import cn.suparking.data.api.beans.ParkingLockModel;
import cn.suparking.data.api.beans.ProjectConfig;
import cn.suparking.data.api.query.ParkEventQuery;
import cn.suparking.data.api.query.ParkQuery;
import cn.suparking.data.dao.entity.DiscountInfoDO;
import cn.suparking.data.dao.entity.ParkingDO;
import cn.suparking.data.dao.entity.ParkingEventDO;
import cn.suparking.data.dao.entity.ParkingTriggerDO;
import cn.suparking.user.api.vo.UserVO;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cn.suparking.customer.api.constant.ParkConstant.SUCCESS;

@Slf4j
@Service
public class ParkServiceImpl implements ParkService {

    @Resource
    private SparkProperties sparkProperties;

    @Resource
    private RabbitmqProperties rabbitmqProperties;

    @Resource
    private SharedProperties sharedProperties;

    private final RabbitTemplate rabbitTemplate;

    private final DataTemplateService dataTemplateService;

    private final UserTemplateService userTemplateService;

    public ParkServiceImpl(final DataTemplateService dataTemplateService, @Qualifier("MQCloudTemplate")final RabbitTemplate rabbitTemplate,
                           final UserTemplateService userTemplateService) {
        this.dataTemplateService = dataTemplateService;
        this.rabbitTemplate = rabbitTemplate;
        this.userTemplateService = userTemplateService;
    }

    @Override
    public List<ParkInfoVO> nearByPark(final LocationDTO locationDTO) {
        JSONObject requestParam = new JSONObject();
        requestParam.put("latitude", locationDTO.getLatitude());
        requestParam.put("longitude", locationDTO.getLongitude());
        requestParam.put("number", locationDTO.getNumber());
        requestParam.put("radius", locationDTO.getRadius());
        JSONObject result = HttpUtils.sendPost(sparkProperties.getUrl() + ParkConstant.INTERFACE_NEARBYPARK, requestParam.toJSONString());
        List<ParkInfoVO> parkInfoVOList = new LinkedList<>();
        return getParkInfoVOS(parkInfoVOList, result);
    }

    @Override
    public List<ParkInfoVO> allLocation() {
        JSONObject result = HttpUtils.sendGet(sparkProperties.getUrl() + ParkConstant.INTERFACE_ALLPARK, null);
        List<ParkInfoVO> parkInfoVOList = new LinkedList<>();
        return getParkInfoVOS(parkInfoVOList, result);
    }

    @Override
    public SpkCommonResult scanCodeQueryFee(final String sign, final ParkFeeQueryDTO parkFeeQueryDTO) {
        // 校验 sign
        if (!invoke(sign, parkFeeQueryDTO.getDeviceNo())) {
            return SpkCommonResult.error(SpkCommonResultMessage.SIGN_NOT_VALID);
        }

        // 获取用户user id.
        UserVO userVO = userTemplateService.getUserByOpenId(parkFeeQueryDTO.getMiniOpenId());
        if (Objects.isNull(userVO)) {
            return SpkCommonResult.error(SpkCommonResultMessage.PARKING_DATE_USER_VALID);
        }
        parkFeeQueryDTO.setUserId(userVO.getId());

        // 校验 设备
        ParkingLockModel parkingLockModel = dataTemplateService.findParkingLock(parkFeeQueryDTO.getDeviceNo());
        if (Objects.isNull(parkingLockModel)) {
            return SpkCommonResult.error(SpkCommonResultMessage.DEVICE_NOT_SETTING);
        }

        // 查询设备对应的停车记录.
        ParkQuery parkQuery = ParkQuery.builder()
                .projectId(Long.valueOf(parkingLockModel.getProjectId()))
                .projectNo(parkingLockModel.getProjectNo())
                .parkId(parkingLockModel.getId())
                .build();
        ParkingDO parkingDO = dataTemplateService.findParking(parkQuery);
        if (Objects.isNull(parkingDO)) {
            return SpkCommonResult.error(SpkCommonResultMessage.ENTER_PARKING_NOT_FOUND);
        }

        ParkingTriggerDO parkingTriggerDO = null;
        if (Objects.nonNull(parkingDO.getEnter())) {
            parkingTriggerDO = dataTemplateService.findParkingTrigger(Long.valueOf(parkingLockModel.getProjectId()), parkingDO.getEnter());
            if (Objects.isNull(parkingTriggerDO)) {
                return SpkCommonResult.error(SpkCommonResultMessage.PARKING_DATA_TRIGGER_VALID);
            }
        }

        List<ParkingEventDO> parkingEvents = null;
        if (StringUtils.isNotBlank(parkingDO.getParkingEvents())) {
            String[] events = parkingDO.getParkingEvents().split(",");
            ParkEventQuery parkEventQuery = ParkEventQuery.builder()
                    .projectId(Long.valueOf(parkingLockModel.getProjectId()))
                    .ids(Stream.of(events).map(String::trim).filter(s -> !s.isEmpty())
                            .map(Long::valueOf).collect(Collectors.toList()))
                    .build();
            parkingEvents = dataTemplateService.findParkingEvents(parkEventQuery);
            if (Objects.isNull(parkingEvents) || parkingDO.getParkingEvents().split(",").length != parkingEvents.size()) {
                return SpkCommonResult.error(SpkCommonResultMessage.PARKING_DATA_EVENT_VALID);
            }
        }

        // 查询项目对应的 ProjectConfig
        ProjectConfig projectConfig = dataTemplateService.getProjectConfig(parkingLockModel.getProjectNo());
        if (Objects.isNull(projectConfig)) {
            return SpkCommonResult.error(SpkCommonResultMessage.PARKING_CONFIG_VALID);
        }

        JSONObject request = new JSONObject();

        // RPC 查询 费用
        request.put("projectConfig", projectConfig);
        request.put("parking", parkingDO);
        // 根据前端传递的 优惠券编号,或者优惠券实体, 重新计费
        if (Objects.nonNull(parkFeeQueryDTO.getDiscountInfo())) {
            request.put("discountInfo", parkFeeQueryDTO.getDiscountInfo());
        } else {
            if (StringUtils.isNotBlank(parkFeeQueryDTO.getDiscountNo())) {
                request.put("discountInfo", getDiscountInfoByNo(parkFeeQueryDTO.getDiscountNo(), parkingDO.getProjectNo()));
            }
            request.put("discountInfo", null);
        }

        request.put("userInfo", parkFeeQueryDTO);
        request.put("enter", parkingTriggerDO);
        request.put("parkingEvents", parkingEvents);
        //TODO 查询费用.
        String retBody = sendRPCQueryFee(request);
        if (StringUtils.isBlank(retBody)) {
            return SpkCommonResult.error(SpkCommonResultMessage.CHARGE_VALID);
        }

        // 组织订单,发送给前端
        ParkFeeRet parkFeeRet = JSON.parseObject(retBody, ParkFeeRet.class);
        if (Objects.isNull(parkFeeRet) || !parkFeeRet.getCode().equals("00000")) {
            return SpkCommonResult.error(SpkCommonResultMessage.CHARGE_CHANGE_DATA_VALID);
        }
        // 生成临时订单号
        String tmpOrderNo = String.valueOf(SnowflakeConfig.snowflakeId());
        log.info("用户: [" + parkFeeRet.getParking().getUserId() + " ], 设备编号: [" + parkFeeRet.getParking().getDeviceNo() + " ],获取临时订单号为: [" + tmpOrderNo + " ]");
        parkFeeRet.setTmpOrderNo(tmpOrderNo);
        Parking parking = parkFeeRet.getParking();
        if (Objects.isNull(parking)) {
            return SpkCommonResult.error(SpkCommonResultMessage.CHARGE_CHANGE_DATA_VALID);
        }

        ParkingOrder parkingOrder = parkFeeRet.getParkingOrder();
        if (Objects.isNull(parkingOrder)) {
            return SpkCommonResult.error(SpkCommonResultMessage.CHARGE_CHANGE_DATA_VALID);
        }
        // 拼接返回前端数据
        ParkFeeQueryVO parkFeeQueryVO = ParkFeeQueryVO.builder()
                .tmpOrderNo(tmpOrderNo)
                .parkingId(parking.getId())
                .projectNo(parking.getProjectNo())
                .userId(parking.getUserId().toString())
                .totalAmount(parkingOrder.getTotalAmount())
                .dueAmount(parkingOrder.getDueAmount())
                .discountedMinutes(parkingOrder.getDiscountedMinutes())
                .discountedAmount(parkingOrder.getDiscountedAmount())
                .carTypeClass(parkingOrder.getCarTypeClass())
                .carTypeName(parkingOrder.getCarTypeName())
                .beginTime(parkingOrder.getBeginTime())
                .endTime(parkingOrder.getEndTime())
                .parkingMinutes(parkingOrder.getParkingMinutes())
                .paidAmount(parkingOrder.getPaidAmount())
                .parkNo(parking.getParkNo())
                .parkName(parking.getParkName())
                .parkId(parking.getParkId())
                .deviceNo(parking.getDeviceNo())
                .expireTime(parkingOrder.getExpireTime())
                .build();

        if (Objects.nonNull(parkingOrder.getDiscountInfo())) {
            parkFeeQueryVO.setDiscountInfo(parkingOrder.getDiscountInfo());
        }

        // 根据UnionId 获取优惠券信息
        List<DiscountInfoDO> discountInfoDOList = getDiscountInfoListByUnionId(parking.getProjectNo(), parkFeeQueryDTO.getUnionId());
        if (Objects.nonNull(discountInfoDOList)) {
            parkFeeQueryVO.setDiscountInfoList(discountInfoDOList);
        }

        // 将临时数据根据 过期时间 存入Redis
        opsValue(parkFeeRet, parking.getParkingConfig().getTxTTL() * 60);
        log.info("用户: [" + parking.getUserId() + " ]" + " 查询车位费用成功:￥ [" + Utils.rmbFenToYuan(parkFeeQueryVO.getDueAmount()) + "] \n");
        log.info("用户: [" + parking.getUserId() + " ]" + " 其他业务数据: [" + parkFeeQueryVO + " ] \n");
        return SpkCommonResult.success(parkFeeQueryVO);
    }

    @Override
    public SpkCommonResult miniToPay(final String sign, final ParkPayDTO parkPayDTO) {
        // 校验 sign
        if (!invoke(sign, parkPayDTO.getTmpOrderNo())) {
            return SpkCommonResult.error(SpkCommonResultMessage.SIGN_NOT_VALID);
        }
        return null;
    }

    @Override
    public SpkCommonResult regularByPark(final RegularLocationDTO regularLocationDTO) {
        JSONObject request = new JSONObject();
        request.put("userId", regularLocationDTO.getUserId());
        JSONObject result = HttpUtils.sendPost(sparkProperties.getUrl() + ParkConstant.INTERFACE_REGULARPARK, request.toJSONString());
        List<ParkInfoVO> parkInfoVOList = new LinkedList<>();
        return SpkCommonResult.success(getParkInfoVOS(parkInfoVOList, result));
    }

    @Override
    public SpkCommonResult projectInfoByDeviceNo(final String sign, final ProjectQueryDTO projectQueryDTO) {
        // 校验 sign
        if (!invoke(sign, projectQueryDTO.getDeviceNo())) {
            return SpkCommonResult.error(SpkCommonResultMessage.SIGN_NOT_VALID);
        }
        JSONObject request = new JSONObject();
        request.put("deviceNo", projectQueryDTO.getDeviceNo());
        JSONObject result = HttpUtils.sendPost(sparkProperties.getUrl() + ParkConstant.INTERFACE_PARKBYDEVICE, request.toJSONString());
        return Optional.ofNullable(result).filter(res -> SUCCESS.equals(res.getString("code"))).map(item ->
                SpkCommonResult.success(JSON.parseObject(item.getString("data"), ParkPayVO.class))).orElseGet(null);
    }

    private String sendRPCQueryFee(final JSONObject params) {
        log.info("Sender 发送RPC 请求,询问停车费用: [" + params + "]");
        MessageProperties properties = new MessageProperties();
        properties.setHeader("method", "PARKING_ORDER_QUERY");
        properties.setHeader("timestamp", DateUtils.timestamp());
        Message message = new Message(params.toJSONString().getBytes(), properties);
        Object receiveMessage = rabbitTemplate.convertSendAndReceive(rabbitmqProperties.getExchange(), "*.shared.#", message);
        if (Objects.isNull(receiveMessage)) {
            log.error("sendRPCQueryFee Failed");
            return null;
        }
        JSONObject retJson = JSON.parseObject(new String((byte[]) receiveMessage));
        log.info("接收计费服务消费返回: [" + retJson.toJSONString() + "]");
        if (!retJson.containsKey("code") || !retJson.getString("code").equals("00000")) {
            return null;
        }
        return retJson.toJSONString();
    }

    /**
     * check sign.
     * @param sign sign.
     * @param deviceNo deviceNo
     * @return Boolean
     */
    private Boolean invoke(final String sign, final String deviceNo) {
        return md5(sharedProperties.getSecret() + deviceNo + DateUtils.currentDate() + sharedProperties.getSecret(), sign);
    }

    /**
     * MD5.
     * @param data the data
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

    /**
     * 根据获取BS 数据组织 C 端所需要的数据.
     * @param parkInfoVOList {@link List}
     * @param result  {@link JSONObject}
     * @return {@link SpkCommonResult}
     */
    private List<ParkInfoVO> getParkInfoVOS(final List<ParkInfoVO> parkInfoVOList, final JSONObject result) {
        return Optional.ofNullable(result).filter(res -> SUCCESS.equals(res.getString("code"))).map(item -> {
            JSONArray jsonArray = item.getJSONArray("list");
            jsonArray.forEach(obj -> {
                try {
                    parkInfoVOList.add(JSON.parseObject(obj.toString(), ParkInfoVO.class));
                } catch (Exception e) {
                    Arrays.stream(e.getStackTrace()).forEach(err -> log.error(err.toString()));
                }
            });
            return parkInfoVOList;
        }).orElse(null);
    }

    /**
     * save data.
     * @param parkFeeRet {@link ParkFeeRet}
     */
    private void opsValue(final ParkFeeRet parkFeeRet, final Integer expireTime) {
        ReactiveRedisUtils.putValue(parkFeeRet.getTmpOrderNo(), JSON.toJSONString(parkFeeRet), expireTime).subscribe(
            flag -> {
                if (flag) {
                    log.info("临时订单 Key= " + parkFeeRet.getTmpOrderNo() + " save redis success! expireTime: " + expireTime);
                } else {
                    log.info("临时订单 Key= " + parkFeeRet.getTmpOrderNo() + " save redis failed!");
                }
            }
        );
    }

    /**
     * 根据wx union id 获取优惠券.
     * @param unionId union id
     * @return {@link List}
     */
    private List<DiscountInfoDO> getDiscountInfoListByUnionId(final String projectNo, final String unionId) {
        Map<String, Object> params = new HashMap<>();
        params.put("projectNo", projectNo);
        params.put("appType", "wx");
        params.put("appUserId", unionId);
        params.put("listType", "notUse");
        log.info("请求未使用优惠券参数: [ " + params + " ]");
        try {
            List<DiscountInfoDO> discountInfoDOList = new ArrayList<>();
            JSONObject result = HttpUtils.sendGet(sharedProperties.getDiscountUrl() + "discountUser/list", params);
            if (Objects.nonNull(result)) {
                List<JSONObject> discountObjList = JSONObject.parseArray(result.getString("notUse_list"), JSONObject.class);
                discountObjList.forEach(item -> {
                    JSONObject discount = item.getJSONObject("discount");
                    DiscountInfoDO discountInfoDO = DiscountInfoDO.builder()
                            .discountNo(discount.getString("discountNo"))
                            .value(discount.getInteger("value"))
                            .quantity(1)
                            .valueType(discount.getString("valueType"))
                            .build();
                    discountInfoDOList.add(discountInfoDO);
                });
            }
            if (discountInfoDOList.size() > 0) {
                return discountInfoDOList;
            }
        } catch (Exception ex) {
            log.error("请求优惠券查询服务异常: " + ex);
        }
        return null;
    }

    /**
     * 根据优惠券编号查询优惠券信息.
     *
     * @param discountNo discount no
     * @return String
     */
    private DiscountInfoDO getDiscountInfoByNo(final String discountNo, final String projectNo) {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("discountNo", discountNo);
            log.info("根据优惠券编号获取优惠券信息请求参数 = [ " + param + " ]");
            JSONObject result = HttpUtils.sendGet(sharedProperties.getDiscountUrl() + "discount/getDiscountByNo", param);
            if (Objects.nonNull(result) && result.containsKey("code") && result.getString("code").equals("200")
                    && result.containsKey("data") && Objects.nonNull(result.getJSONObject("data"))) {
                log.info("获取优惠券信息： [ " + result.toJSONString() + " ]");
                DiscountCustomer discountCustomer = JSON.parseObject(result.getString("data"), DiscountCustomer.class);
                if (Objects.nonNull(discountCustomer) && discountCustomer.getEnabled()
                        && discountCustomer.getUsedCount() < discountCustomer.getMaxAvailableCount()
                        && discountCustomer.getProjectNo().equals(projectNo) && !judgeDiscountDate(discountCustomer.getExpireDate())) {
                    return DiscountInfoDO.builder()
                            .discountNo(discountCustomer.getDiscountNo())
                            .valueType(discountCustomer.getValueType())
                            .value(discountCustomer.getValue())
                            .quantity(1)
                            .build();

                } else {
                    log.error("根据优惠券编号: " + discountNo + " 查询经过判断优惠券无法使用: " + result.getString("data"));
                }
            }
        } catch (Exception e) {
            log.error("请求优惠劵信息查询服务异常----------->msg = " + e);
        }
        return null;
    }

    /**
     * 判断优惠券是否过期.
     * @param expireTime 过期时间
     * @return {@link Boolean}
     */
    private Boolean judgeDiscountDate(final Long expireTime) {
        if (DateUtils.getCurrentSecond() > expireTime) {
            return true;
        }
        return false;
    }
}
