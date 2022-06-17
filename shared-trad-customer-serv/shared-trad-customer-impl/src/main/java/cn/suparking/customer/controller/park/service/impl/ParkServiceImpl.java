package cn.suparking.customer.controller.park.service.impl;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.common.api.utils.DateUtils;
import cn.suparking.common.api.utils.HttpUtils;
import cn.suparking.common.api.utils.SpkCommonResultMessage;
import cn.suparking.customer.api.beans.ParkFeeQueryDTO;
import cn.suparking.customer.api.constant.ParkConstant;
import cn.suparking.customer.beans.park.LocationDTO;
import cn.suparking.customer.configuration.properties.RabbitmqProperties;
import cn.suparking.customer.configuration.properties.SharedProperties;
import cn.suparking.customer.configuration.properties.SparkProperties;
import cn.suparking.customer.controller.park.service.ParkService;
import cn.suparking.customer.feign.data.DataTemplateService;
import cn.suparking.customer.feign.user.UserTemplateService;
import cn.suparking.customer.vo.park.ParkInfoVO;
import cn.suparking.data.api.beans.ParkingLockModel;
import cn.suparking.data.api.query.ParkEventQuery;
import cn.suparking.data.api.query.ParkQuery;
import cn.suparking.data.dao.entity.DiscountInfoDO;
import cn.suparking.data.dao.entity.ParkingDO;
import cn.suparking.data.dao.entity.ParkingEventDO;
import cn.suparking.data.dao.entity.ParkingTriggerDO;
import cn.suparking.data.dao.entity.ValueType;
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
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
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
        // 查询是否存在优惠券.
        DiscountInfoDO discountInfoDO = DiscountInfoDO.builder()
                .discountNo("1111111111111111111")
                .quantity(2)
                .value(100)
                .valueType(ValueType.AMOUNT)
                .build();
        JSONObject request = new JSONObject();

        // RPC 查询 费用
        request.put("parking", parkingDO);
        request.put("discountInfo", discountInfoDO);
        request.put("userInfo", parkFeeQueryDTO);
        request.put("enter", parkingTriggerDO);
        request.put("parkingEvents", parkingEvents);
        //TODO 查询费用.
        String retBody = sendRPCQueryFee(request);
        if (StringUtils.isBlank(retBody)) {
            return SpkCommonResult.error(SpkCommonResultMessage.CHARGE_VALID);
        }
        // 组织订单,发送给前端
        return null;
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
        if (!retJson.containsKey("code") || retJson.getInteger("code").equals(200)) {
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

    private List<ParkInfoVO> getParkInfoVOS(final List<ParkInfoVO> parkInfoVOList, final JSONObject result) {
        return Optional.ofNullable(result).filter(res -> SUCCESS.equals(res.getString("code"))).map(item -> {
            JSONArray jsonArray = item.getJSONArray("list");
            jsonArray.forEach(obj -> {
                try {
                    parkInfoVOList.add(JSON.toJavaObject((JSONObject) obj, ParkInfoVO.class));
                } catch (Exception e) {
                    Arrays.stream(e.getStackTrace()).forEach(err -> log.error(err.toString()));
                }
            });
            return parkInfoVOList;
        }).orElse(null);
    }
}
