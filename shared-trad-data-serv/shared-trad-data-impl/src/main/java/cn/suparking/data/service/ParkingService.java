package cn.suparking.data.service;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.data.api.beans.ParkingDTO;
import cn.suparking.data.api.query.ParkingQueryDTO;
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

    /**
     * 根据条件获取停车记录.
     *
     * @param parkingQueryDTO String
     * @return {@link ParkingQueryDTO}
     */
    SpkCommonResult list(ParkingQueryDTO parkingQueryDTO);

    ParkingDO findByPayParkingId(String payParkingId);
}
