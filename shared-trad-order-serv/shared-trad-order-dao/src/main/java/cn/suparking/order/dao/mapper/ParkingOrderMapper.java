package cn.suparking.order.dao.mapper;

import cn.suparking.order.dao.entity.ParkingOrderDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ParkingOrderMapper {

    /**
     * 根据id查找临停订单.
     *
     * @param id primary id
     * @return {@linkplain ParkingOrderDO}
     */
    ParkingOrderDO selectById(String id);

    /**
     * 新增临停订单.
     *
     * @param parkingOrderDO {@linkplain ParkingOrderDO}
     * @return int
     */
    int insert(ParkingOrderDO parkingOrderDO);

    /**
     * 更新临停订单.
     *
     * @param parkingOrderDO {@linkplain ParkingOrderDO}
     * @return int
     */
    int update(ParkingOrderDO parkingOrderDO);

    /**
     * 查询规定范围内的订单数据.
     * @param params {@link Map}
     * @return {@link List}
     */
    List<ParkingOrderDO> findByUserIdsAndBeginTimeOrEndTimeRange(Map<String, Object> params);

    /**
     * 查询规定范围内的订单数据.
     * @param params {@link Map}
     * @return {@link List}
     */
    List<ParkingOrderDO> findByUserIdsAndEndTimeRange(Map<String, Object> params);

    /**
     * 查询规定范围内的订单数据.
     * @param params {@link Map}
     * @return {@link List}
     */
    ParkingOrderDO findNextAggregateBeginTime(Map<String, Object> params);
}
