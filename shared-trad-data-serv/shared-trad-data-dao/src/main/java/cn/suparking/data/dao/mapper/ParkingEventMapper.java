package cn.suparking.data.dao.mapper;

import cn.suparking.data.dao.entity.ParkingEventDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ParkingEventMapper {
    /**
     * 通过ID 获取 停车触发信息.
     * @param id parking event id
     * @return {@link ParkingEventDO}
     */
    ParkingEventDO selectById(String id);


    /**
     * insert Parking Event.
     * @param parkingEventDO {@link ParkingEventDO}
     * @return int
     */
    int insert(ParkingEventDO parkingEventDO);

    /**
     * 更新 Parking Event.
     * @param parkingEventDO {@link ParkingEventDO}
     * @return int
     */
    int update(ParkingEventDO parkingEventDO);
}
