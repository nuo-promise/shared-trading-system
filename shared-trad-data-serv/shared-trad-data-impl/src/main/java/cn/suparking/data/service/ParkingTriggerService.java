package cn.suparking.data.service;

import cn.suparking.data.api.beans.ParkingTriggerDTO;
import cn.suparking.data.dao.entity.ParkingTriggerDO;

public interface ParkingTriggerService {

    /**
     * find Parking Trigger by id.
     * @param id id
     * @return {@link ParkingTriggerDO}
     */
    ParkingTriggerDO findById(String id);

    /**
     * create or update parking.
     * @param parkingTriggerDTO {@link ParkingTriggerDTO}
     * @return int
     */
    Long createOrUpdate(ParkingTriggerDTO parkingTriggerDTO);
}
