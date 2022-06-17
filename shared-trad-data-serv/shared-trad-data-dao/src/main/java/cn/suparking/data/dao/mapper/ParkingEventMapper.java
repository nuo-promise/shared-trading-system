package cn.suparking.data.dao.mapper;

import cn.suparking.data.dao.entity.ParkingEventDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

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

    /**
     * 根据 projectId, event ids 查询停车事件.
     * @param params {@link Map}
     * @return {@link List}
     */
    List<ParkingEventDO> findByProjectIdAndIds(Map<String, Object> params);
}
