package cn.suparking.data.service;

import cn.suparking.data.api.beans.ParkingTriggerDTO;
import cn.suparking.data.dao.entity.ParkingTriggerDO;

public interface ParkingTriggerService {

    /**
     * find Parking Trigger by id.
     * @param id id
     * @return {@link ParkingTriggerDO}
     */
    ParkingTriggerDO findById(Long id);

    /**
     * create or update parking.
     * @param parkingTriggerDTO {@link ParkingTriggerDTO}
     * @return int
     */
    Long createOrUpdate(ParkingTriggerDTO parkingTriggerDTO);


    /**
     * 根据项目ID,triggerid 查询trigger事件.
     * @param projectId 项目 id
     * @param triggerId 触发 id
     * @return {@link ParkingTriggerDO}
     */
    ParkingTriggerDO findByProjectIdAndId(Long projectId, Long triggerId);
}
