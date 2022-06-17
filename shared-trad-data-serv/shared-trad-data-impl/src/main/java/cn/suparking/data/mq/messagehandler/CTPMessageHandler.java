package cn.suparking.data.mq.messagehandler;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.common.api.configuration.SnowflakeConfig;
import cn.suparking.common.api.exception.SpkCommonCode;
import cn.suparking.common.api.utils.DateUtils;
import cn.suparking.data.Application;
import cn.suparking.data.api.beans.EventType;
import cn.suparking.data.api.beans.ParkStatusModel;
import cn.suparking.data.api.beans.ParkingDTO;
import cn.suparking.data.api.beans.ParkingEventDTO;
import cn.suparking.data.api.beans.ParkingLockModel;
import cn.suparking.data.api.beans.ParkingState;
import cn.suparking.data.api.beans.ParkingTriggerDTO;
import cn.suparking.data.api.beans.ProjectConfig;
import cn.suparking.data.api.constant.DataConstant;
import cn.suparking.data.dao.entity.ParkingDO;
import cn.suparking.data.exception.SharedTradException;
import cn.suparking.data.mq.messageTemplate.DeviceMessageThread;
import cn.suparking.data.mq.messageTemplate.MessageTemplate;
import cn.suparking.data.service.ParkingEventService;
import cn.suparking.data.service.ParkingService;
import cn.suparking.data.service.ParkingTriggerService;
import cn.suparking.data.tools.ProjectConfigUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.transaction.annotation.ShardingSphereTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

import static cn.suparking.data.api.constant.DataConstant.CTP_TYPE;

@Slf4j
@Component("MQ_CTP_PARK_STATUS")
public class CTPMessageHandler extends MessageHandler {

    private final ParkingService parkingService;

    private final ParkingTriggerService parkingTriggerService;

    private final ParkingEventService parkingEventService;

    private final DeviceMessageThread deviceMessageThread = Application.getBean("DeviceMessageThread", DeviceMessageThread.class);
    /**
     * constructor.
     */
    public CTPMessageHandler(final ParkingService parkingService, final ParkingTriggerService parkingTriggerService,
                             final ParkingEventService parkingEventService) {
        super("MQ_CTP_PARK_STATUS_RET");
        this.parkingService = parkingService;
        this.parkingTriggerService = parkingTriggerService;
        this.parkingEventService = parkingEventService;
    }

    @Override
    @ShardingSphereTransactionType(TransactionType.BASE)
    public SpkCommonResult consumeMessage(final MessageTemplate messageTemplate) throws SharedTradException {
        MessageProperties messageProperties = messageTemplate.getMessage().getMessageProperties();
        Map<String, Object> headers = messageProperties.getHeaders();

        String from = (String) headers.get("from");
        String topic = (String) headers.get("topic");
        if (CTP_TYPE.equals(from) && topic.contains("device.status")) {
            SpkCommonResult result = invoke(messageTemplate.getParkingLockModel(), messageTemplate.getParkStatusModel());
            if (Objects.isNull(result) || !result.getCode().equals(SpkCommonCode.SUCCESS)) {
                throw new SharedTradException("CTP Save Enter Parking Records Error.");
            }
            return SpkCommonResult.success("CTP MessageHandler ConsumeMessage Success.");
        }
        return SpkCommonResult.error("CTPMessageHandler ConsumeMessage Error");
    }

    /**
     * save park status enter.
     * @param parkingLockModel {@link ParkingLockModel}
     * @param parkStatusModel {@link ParkStatusModel}
     * @return {@Link SpkCommonResult}
     */
    public SpkCommonResult invoke(final ParkingLockModel parkingLockModel, final ParkStatusModel parkStatusModel) {
        /**
         * 1. 如果是 车辆入位 状态,那么比对 表中对于相同项目,相同车位 状态是 入位的时间间隔,如果相差间隔在设置范围内,则忽略,如果超过则认为新的入场
         */
        Object obj = ProjectConfigUtils.poll(parkingLockModel.getProjectNo(), DataConstant.RESOURCE_PROJECT);
        if (Objects.isNull(obj)) {
            log.error("Shared Trad Data ProjectConfig Not Exists");
            return SpkCommonResult.success("Shared Trad Data ProjectConfig Not Exists");
        }

        // 判断事件是入场 -> 查询数据库时间最近的记录 ; 如果是入场,那么比对两者时间差
        JSONObject json = JSON.parseObject((String) obj, JSONObject.class);
        if (Objects.isNull(json) || Objects.isNull(json.getJSONObject("parkingConfig"))) {
            log.warn("操作停车记录时候,未获取到项目: " + parkingLockModel.getProjectNo() + ", 配置信息,默认做落库操作");
            return SpkCommonResult.success("Shared Trad Data ProjectConfig Not Exists");
        }

        ProjectConfig projectConfig = JSON.parseObject(json.getJSONObject("parkingConfig").toJSONString(), ProjectConfig.class);
        if (Objects.isNull(projectConfig)) {
            log.warn("操作停车记录时候,未获取到项目: " + parkingLockModel.getProjectNo() + ", 配置信息,默认做落库操作");
            return SpkCommonResult.success("Shared Trad Data ProjectConfig Not Exists");
        }

        // 查询 当前场库,当前车位 最近一次记录.
        ParkingDO parkingDO = deviceMessageThread.getLatestParkingDO(parkingLockModel);
        if (Objects.nonNull(parkingDO) && notimeout(parkingDO.getLatestTriggerTime(), projectConfig.getMinIntervalForDupPark())
                && parkStatusModel.getParkStatus() && parkingDO.getParkingState().equals(ParkingState.ENTERED.name())) {
            return SpkCommonResult.success("Shared Trad minIntervalDupPark no timeout.");
        }
        // 入场数据落库.
        parkingLockModel.setParkId(parkingLockModel.getId());
        return saveEnterParking(parkingLockModel, parkStatusModel);
    }

    /**
     * 批量插入数据记录.
     * @param parkingLockModel {@link ParkingLockModel}
     * @param parkStatusModel {@link ParkStatusModel}
     */
    private SpkCommonResult saveEnterParking(final ParkingLockModel parkingLockModel, final ParkStatusModel parkStatusModel) {
        // 落库数据
        Long currentTime = DateUtils.getCurrentSecond();
        ParkingTriggerDTO parkingTriggerDTO = ParkingTriggerDTO.builder()
                .projectId(parkingLockModel.getProjectId())
                .recogTime(currentTime)
                .openTime(currentTime)
                .deviceNo(parkingLockModel.getDeviceNo())
                .parkId(parkingLockModel.getParkId())
                .parkName(parkingLockModel.getParkName())
                .parkNo(parkingLockModel.getParkNo())
                .inSubAreaId(parkingLockModel.getInSubAreaId())
                .inSubAreaName(parkingLockModel.getInSubAreaName())
                .outSubAreaId(parkingLockModel.getOutSubAreaId())
                .outSubAreaName(parkingLockModel.getOutSubAreaName())
                .operator("system")
                .build();
        Long parkingTriggerId = parkingTriggerService.createOrUpdate(parkingTriggerDTO);
        if (parkingTriggerId == -1L) {
            log.error("操作停车记录数据库 ParkingTrigger 失败 " + JSON.toJSONString(parkingTriggerDTO));
            return SpkCommonResult.error("操作停车记录数据库 ParkingTrigger 失败");
        }
        // 存储event.
        ParkingEventDTO parkingEventDTO = ParkingEventDTO.builder()
                .projectId(parkingLockModel.getProjectId())
                .eventType(EventType.EV_ENTER.name())
                .eventTime(currentTime)
                .deviceNo(parkingLockModel.getDeviceNo())
                .parkId(parkingLockModel.getParkId())
                .parkNo(parkingLockModel.getParkNo())
                .parkName(parkingLockModel.getParkName())
                .recogId(parkingTriggerId.toString())
                .inSubAreaId(parkingLockModel.getInSubAreaId())
                .inSubAreaName(parkingLockModel.getOutSubAreaName())
                .outSubAreaId(parkingLockModel.getInSubAreaId())
                .outSubAreaName(parkingLockModel.getOutSubAreaName())
                .operator("system")
                .build();
        Long parkingEventId = parkingEventService.createOrUpdate(parkingEventDTO);
        if (parkingEventId == -1L) {
            log.error("操作停车记录数据库 ParkingEvent 失败 " + JSON.toJSONString(parkingTriggerDTO));
            return SpkCommonResult.error("操作停车记录数据库 TriggerEvent 失败");
        }
        // 存入Parking 数据
        LinkedList<String> events = new LinkedList<>();
        events.add(parkingEventId.toString());
        ParkingDTO parkingDTO = ParkingDTO.builder()
                .projectId(parkingLockModel.getProjectId())
                .parkId(parkingLockModel.getParkId())
                .parkNo(parkingLockModel.getParkNo())
                .parkName(parkingLockModel.getParkName())
                .deviceNo(parkingLockModel.getDeviceNo())
                .enter(parkingTriggerId)
                .parkingEvents(events)
                .firstEnterTriggerTime(currentTime)
                .latestTriggerTime(currentTime)
                .parkingState(String.valueOf(ParkingState.ENTERED))
                .allowCorrect(1)
                .valid(1)
                .payParkingId(String.valueOf(SnowflakeConfig.snowflakeId()))
                .parkingMinutes(0)
                .projectNo(parkingLockModel.getProjectNo())
                .creator("system")
                .build();
        if (parkingService.createOrUpdate(parkingDTO) > 0) {
            return SpkCommonResult.success("操作成功");
        }
        return SpkCommonResult.error("操作失败");
    }

    private Boolean notimeout(final Long latestTriggerTime, final Long minIntervalForDupPark) {
        return (DateUtils.getCurrentMillis() - latestTriggerTime * 1000) < minIntervalForDupPark;
    }
}
