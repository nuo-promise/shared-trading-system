package cn.suparking.data.service;

import cn.suparking.data.api.beans.ParkingDTO;
import cn.suparking.data.dao.entity.ParkingDO;

import java.util.Map;

public interface ParkingService {

    /**
     * find Parking by id.
     * @param id id
     * @return {@link ParkingDO}
     */
    ParkingDO findById(Long id);

    /**
     * create or update parking.
     * @param parkingDTO {@link ParkingDTO}
     * @return int
     */
    Integer createOrUpdate(ParkingDTO parkingDTO);


    /**
     * find parking by projectId and projectNo and parkId Desc latest trigger time.
     * @param params {@likn ParkingDTO}
     * @return {@link ParkingDO}
     */
    ParkingDO findByProjectIdAndParkId(Map<String, Object> params);
}
