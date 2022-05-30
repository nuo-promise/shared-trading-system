package cn.suparking.order.mapper;

import cn.suparking.order.entity.ParkingOrderDO;
import org.apache.ibatis.annotations.Mapper;

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
}
