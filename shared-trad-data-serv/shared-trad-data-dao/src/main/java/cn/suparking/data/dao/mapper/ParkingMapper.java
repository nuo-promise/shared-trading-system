package cn.suparking.data.dao.mapper;

import cn.suparking.data.dao.entity.ParkingDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface ParkingMapper {

    /**
     * 通过ID 获取 停车信息.
     * @param id parking id
     * @return {@link ParkingDO}
     */
    ParkingDO selectById(Long id);


    /**
     * insert Parking.
     * @param parkingDO {@link ParkingDO}
     * @return int
     */
    int insert(ParkingDO parkingDO);

    /**
     * 更新 Parking.
     * @param parkingDO {@link ParkingDO}
     * @return int
     */
    int update(ParkingDO parkingDO);


    /**
     * get parking by project id.
     * @param params {@link Map}
     * @return {@link ParkingDO}
     */
    ParkingDO findByProjectIdAndParkId(Map<String, Object> params);
}
