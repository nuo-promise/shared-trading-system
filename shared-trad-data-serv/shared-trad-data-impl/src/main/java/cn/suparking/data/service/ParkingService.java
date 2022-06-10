package cn.suparking.data.service;

import cn.suparking.data.api.beans.ParkingDTO;
import cn.suparking.data.dao.entity.ParkingDO;

public interface ParkingService {

    /**
     * find Parking by id.
     * @param id id
     * @return {@link ParkingDO}
     */
    ParkingDO findById(String id);

    /**
     * create or update parking.
     * @param parkingDTO {@link ParkingDTO}
     * @return int
     */
    Integer createOrUpdate(ParkingDTO parkingDTO);
}
