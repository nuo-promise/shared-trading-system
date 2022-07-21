package cn.suparking.order.dao.mapper;

import cn.suparking.order.api.beans.ParkingOrderQueryDTO;
import cn.suparking.order.dao.entity.DiscountInfoDO;
import cn.suparking.order.dao.entity.ParkingOrderDO;
import cn.suparking.order.dao.vo.ParkingOrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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

    List<String> detailParkingOrder(@Param("userId") Long userId);

    /**
     * 查询规定范围内的订单数据.
     *
     * @param params {@link Map}
     * @return {@link List}
     */
    List<ParkingOrderDO> findByUserIdsAndBeginTimeOrEndTimeRange(Map<String, Object> params);

    /**
     * 查询规定范围内的订单数据.
     *
     * @param params {@link Map}
     * @return {@link List}
     */
    List<ParkingOrderDO> findByUserIdsAndEndTimeRange(Map<String, Object> params);

    /**
     * 查询规定范围内的订单数据.
     *
     * @param params {@link Map}
     * @return {@link List}
     */
    ParkingOrderDO findNextAggregateBeginTime(Map<String, Object> params);

    /**
     * 根据条件查询订单.
     *
     * @param parkingOrderQueryDTO 订单详情信息
     * @return Integer
     */
    List<ParkingOrderVO> list(ParkingOrderQueryDTO parkingOrderQueryDTO);

    /**
     * 查找某个用户指定时间内的订单.
     * @param params {@link Map}
     * @return {@link List}
     */
    List<ParkingOrderDO> findOrderByUserId(Map<String, Object> params);
}
