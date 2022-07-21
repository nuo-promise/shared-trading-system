package cn.suparking.data.service.impl;

import cn.suparking.common.api.beans.SpkCommonResult;
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
import cn.suparking.data.api.beans.PublishData;
import cn.suparking.data.api.constant.DataConstant;
import cn.suparking.data.api.parkfee.Parking;
import cn.suparking.data.api.query.ParkQuery;
import cn.suparking.data.dao.entity.ParkingDO;
import cn.suparking.data.dao.entity.ParkingTriggerDO;
import cn.suparking.data.dao.mapper.ParkingMapper;
import cn.suparking.data.mq.messageTemplate.DeviceMessageThread;
import cn.suparking.data.mq.messagehandler.CTPMessageHandler;
import cn.suparking.data.service.CtpDataService;
import cn.suparking.data.service.ParkingEventService;
import cn.suparking.data.service.ParkingService;
import cn.suparking.data.service.ParkingTriggerService;
import cn.suparking.data.tools.ProjectConfigUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class CtpDataServiceImpl implements CtpDataService {

    private final CTPMessageHandler ctpMessageHandler;

    private final ParkingMapper parkingMapper;

    private final ParkingTriggerService parkingTriggerService;

    private final ParkingEventService parkingEventService;

    private final ParkingService parkingService;

    private final DeviceMessageThread deviceMessageThread = Application.getBean("DeviceMessageThread", DeviceMessageThread.class);

    public CtpDataServiceImpl(final CTPMessageHandler ctpMessageHandler, final ParkingMapper parkingMapper,
                              final ParkingTriggerService parkingTriggerService,
                              final ParkingEventService parkingEventService, final ParkingService parkingService) {
        this.ctpMessageHandler = ctpMessageHandler;
        this.parkingMapper = parkingMapper;
        this.parkingTriggerService = parkingTriggerService;
        this.parkingEventService = parkingEventService;
        this.parkingService = parkingService;
    }

    @Override
    public SpkCommonResult parkStatus(final JSONObject obj) {
        String body = obj.getString("message");
        if (StringUtils.isEmpty(body) || !obj.getString("type").equals("CTP")) {
            return SpkCommonResult.error("parkStatus Request Error");
        }
        PublishData publishData = JSON.parseObject(body, PublishData.class);
        String lockCode = "";
        ParkingLockModel parkingLockModel = null;
        ParkStatusModel parkStatusModel = JSON.parseObject(publishData.getData(), ParkStatusModel.class);
        lockCode = parkStatusModel.getLockCode();
        if (StringUtils.isNotBlank(lockCode)) {
            parkingLockModel = deviceMessageThread.getParkInfoByDeviceNo(lockCode);
        }

        if (Objects.isNull(parkingLockModel)) {
            log.error("Shard Data ParkStatus parkingLockModel null");
            return SpkCommonResult.error("Shard Data ParkStatus parkingLockModel null");
        }
        return ctpMessageHandler.invoke(parkingLockModel, parkStatusModel);
    }

    @Override
    public SpkCommonResult searchBoardStatus(final JSONObject params) {
        String deviceNo = params.getString("deviceNo");
        if (StringUtils.isBlank(deviceNo)) {
            return SpkCommonResult.error("请输入正确的设备号");
        }
        ParkingLockModel parkingLockModel = deviceMessageThread.getParkInfoByDeviceNo(deviceNo);
        if (Objects.isNull(parkingLockModel)) {
            return SpkCommonResult.error(deviceNo + " ,系统不存在,忽略");
        }
        JSONObject result = new JSONObject();
        ParkingDO parkingDO = deviceMessageThread.getLatestParkingDO(parkingLockModel);
        if (Objects.nonNull(parkingDO) && parkingDO.getParkingState().equals(ParkingState.ENTERED.name())) {
            result.put("status", 0);
        } else {
            result.put("status", 1);
        }
        return SpkCommonResult.success(result);
    }

    @Override
    public ParkingLockModel findParkingLock(final String deviceNo) {
        return deviceMessageThread.getParkInfoByDeviceNo(deviceNo);
    }

    @Override
    public ParkingDO findParking(final ParkQuery parkQuery) {
        Map<String, Object> sqlParams = new HashMap<>();
        sqlParams.put("projectId", parkQuery.getProjectId());
        sqlParams.put("projectNo", parkQuery.getProjectNo());
        sqlParams.put("parkId", parkQuery.getParkId());
        return parkingMapper.findByParkIdAndParkState(sqlParams);
    }

    @Override
    public ProjectConfig getProjectConfig(final String projectNo) {
        Object obj = ProjectConfigUtils.poll(projectNo, DataConstant.RESOURCE_PROJECT);
        if (Objects.nonNull(obj)) {
            // 判断事件是入场 -> 查询数据库时间最近的记录 ; 如果是入场,那么比对两者时间差
            JSONObject json = JSON.parseObject((String) obj, JSONObject.class);
            if (Objects.nonNull(json) && json.containsKey("parkingConfig") && Objects.nonNull(json.getJSONObject("parkingConfig"))) {
                return JSON.parseObject(json.getJSONObject("parkingConfig").toJSONString(), ProjectConfig.class);
            }
        }
        return null;
    }

    @Override
    public Boolean createAndUpdateParking(final Parking parking) {
        // 生成 trigger
        // 生成 event.
        // 更新 parking
        ParkingTriggerDO enterTrigger = parkingTriggerService.findById(Long.valueOf(parking.getEnter().getRecogId()));
        if (Objects.isNull(enterTrigger)) {
            log.error("createAndUpdateParking enterTrigger null");
            return false;
        }
        Long currentTime = DateUtils.getCurrentSecond();
        ParkingTriggerDTO parkingTriggerDTO = ParkingTriggerDTO.builder()
                .projectId(enterTrigger.getProjectId().toString())
                .recogTime(currentTime)
                .openTime(currentTime)
                .deviceNo(enterTrigger.getDeviceNo())
                .parkId(enterTrigger.getParkId())
                .parkName(enterTrigger.getParkName())
                .parkNo(enterTrigger.getParkNo())
                .inSubAreaId(enterTrigger.getInSubAreaId())
                .inSubAreaName(enterTrigger.getInSubAreaName())
                .outSubAreaId(enterTrigger.getOutSubAreaId())
                .outSubAreaName(enterTrigger.getOutSubAreaName())
                .operator("system")
                .build();
        Long parkingTriggerId = parkingTriggerService.createOrUpdate(parkingTriggerDTO);
        if (parkingTriggerId == -1L) {
            log.error("操作停车记录数据库 ParkingTrigger 失败 " + JSON.toJSONString(parkingTriggerDTO));
            return false;
        }
        // 存储event.
        ParkingEventDTO parkingEventDTO = ParkingEventDTO.builder()
                .projectId(enterTrigger.getProjectId().toString())
                .eventType(EventType.EV_LEAVE.name())
                .eventTime(currentTime)
                .deviceNo(enterTrigger.getDeviceNo())
                .parkId(enterTrigger.getParkId())
                .parkNo(enterTrigger.getParkNo())
                .parkName(enterTrigger.getParkName())
                .recogId(parkingTriggerId.toString())
                .inSubAreaId(enterTrigger.getInSubAreaId())
                .inSubAreaName(enterTrigger.getOutSubAreaName())
                .outSubAreaId(enterTrigger.getInSubAreaId())
                .outSubAreaName(enterTrigger.getOutSubAreaName())
                .operator("system")
                .build();
        Long parkingEventId = parkingEventService.createOrUpdate(parkingEventDTO);
        if (parkingEventId == -1L) {
            log.error("操作停车记录数据库 ParkingEvent 失败 " + JSON.toJSONString(parkingTriggerDTO));
            return false;
        }

        ParkingDO parkingDO = parkingService.findById(Long.valueOf(parking.getId()));
        if (Objects.isNull(parkingDO)) {
            log.error("createAndUpdateParking parkingDO null");
            return false;
        }
        // 存入Parking 数据
        LinkedList<String> events = new LinkedList<>();
        events.add(parking.getEnter().getRecogId());
        events.add(parkingEventId.toString());
        ParkingDTO parkingDTO = ParkingDTO.builder()
                .id(parking.getId())
                .projectId(enterTrigger.getProjectId().toString())
                .userId(parking.getUserId().toString())
                .parkId(enterTrigger.getParkId())
                .parkNo(enterTrigger.getParkNo())
                .parkName(enterTrigger.getParkName())
                .deviceNo(enterTrigger.getDeviceNo())
                .carGroupId(parking.getCarGroupId())
                .specialType(parking.getSpecialType())
                .enter(Long.valueOf(parking.getEnter().getRecogId()))
                .leave(parkingTriggerId)
                .parkingEvents(events)
                .firstEnterTriggerTime(parking.getFirstEnterTriggerTime())
                .latestTriggerTime(currentTime)
                .latestTriggerParkId(enterTrigger.getParkId())
                .latestTriggerTemp(Objects.nonNull(parking.getLatestTriggerTemp()) ? parking.getLatestTriggerTemp() ? 0 : 1 : 1)
                .latestTriggerTypeClass(parking.getLatestTriggerTypeClass())
                .latestTriggerTypeName(parking.getLatestTriggerTypeName())
                .parkingState(String.valueOf(ParkingState.LEFT))
                .allowCorrect(1)
                .valid(1)
                .payParkingId(parking.getPayParkingId())
                .parkingMinutes(parking.getParkingMinutes())
                .projectNo(parking.getProjectNo())
                .matchedParkingId(Long.valueOf(parking.getId()))
                .creator("system")
                .build();
        if (parkingService.createOrUpdate(parkingDTO) > 0) {
            return true;
        }
        return false;
    }
}
