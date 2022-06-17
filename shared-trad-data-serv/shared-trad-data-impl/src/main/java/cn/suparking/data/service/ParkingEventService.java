package cn.suparking.data.service;

import cn.suparking.data.api.beans.ParkingEventDTO;
import cn.suparking.data.api.query.ParkEventQuery;
import cn.suparking.data.dao.entity.ParkingEventDO;

import java.util.List;
import java.util.Map;

public interface ParkingEventService {
    /**
     * find Parking Event by id.
     * @param id id
     * @return {@link ParkingEventDO}
     */
    ParkingEventDO findById(String id);

    /**
     * create or update parking.
     * @param parkingEventDTO {@link ParkingEventDTO}
     * @return int
     */
    Long createOrUpdate(ParkingEventDTO parkingEventDTO);

    /**
     * 根据 项目 编号,和 Event 查询事件.
     * @param parkEventQuery {@link ParkEventQuery}
     * @return {@link List}
     */
    List<ParkingEventDO> findParkingEvents(ParkEventQuery parkEventQuery);
}
