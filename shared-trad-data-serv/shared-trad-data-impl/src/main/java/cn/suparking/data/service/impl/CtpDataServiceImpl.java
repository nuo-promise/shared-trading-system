package cn.suparking.data.service.impl;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.data.Application;
import cn.suparking.data.api.beans.ParkStatusModel;
import cn.suparking.data.api.beans.ParkingLockModel;
import cn.suparking.data.api.beans.ParkingState;
import cn.suparking.data.api.beans.PublishData;
import cn.suparking.data.api.query.ParkQuery;
import cn.suparking.data.dao.entity.ParkingDO;
import cn.suparking.data.dao.entity.ParkingTriggerDO;
import cn.suparking.data.dao.mapper.ParkingMapper;
import cn.suparking.data.dao.mapper.ParkingTriggerMapper;
import cn.suparking.data.mq.messageTemplate.DeviceMessageThread;
import cn.suparking.data.mq.messagehandler.CTPMessageHandler;
import cn.suparking.data.service.CtpDataService;
import cn.suparking.data.service.ParkingTriggerService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class CtpDataServiceImpl implements CtpDataService {

    private final CTPMessageHandler ctpMessageHandler;

    private final ParkingMapper parkingMapper;

    private final ParkingTriggerMapper parkingTriggerMapper;

    private final DeviceMessageThread deviceMessageThread = Application.getBean("DeviceMessageThread", DeviceMessageThread.class);

    public CtpDataServiceImpl(final CTPMessageHandler ctpMessageHandler, final ParkingMapper parkingMapper, final ParkingTriggerMapper parkingTriggerMapper) {
        this.ctpMessageHandler = ctpMessageHandler;
        this.parkingMapper = parkingMapper;
        this.parkingTriggerMapper = parkingTriggerMapper;
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
}
