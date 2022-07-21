package cn.suparking.data.dao.mapper;

import cn.suparking.data.dao.entity.ParkingTriggerDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface ParkingTriggerMapper {
    /**
     * 通过ID 获取 停车信息.
     * @param id parking trigger id
     * @return {@link ParkingTriggerDO}
     */
    ParkingTriggerDO selectById(Long id);


    /**
     * insert Parking Trigger.
     * @param parkingTriggerDO {@link ParkingTriggerDO}
     * @return int
     */
    int insert(ParkingTriggerDO parkingTriggerDO);

    /**
     * 更新 Parking trigger.
     * @param parkingTriggerDO {@link ParkingTriggerDO}
     * @return int
     */
    int update(ParkingTriggerDO parkingTriggerDO);

    /**
     * 根据项目ID,triggerid 查询trigger事件.
     * @param params {@link Map}
     * @return {@link ParkingTriggerDO}
     */
    ParkingTriggerDO findByProjectIdAndId(Map<String, Object> params);
}
