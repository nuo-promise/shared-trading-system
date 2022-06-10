package cn.suparking.data.service;

import cn.suparking.data.api.beans.ParkingEventDTO;
import cn.suparking.data.dao.entity.ParkingEventDO;

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
    Integer createOrUpdate(ParkingEventDTO parkingEventDTO);
}
