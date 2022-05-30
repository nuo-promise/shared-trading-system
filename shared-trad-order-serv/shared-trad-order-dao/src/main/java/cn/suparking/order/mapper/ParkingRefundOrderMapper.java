package cn.suparking.order.mapper;

import cn.suparking.order.entity.ParkingRefundOrderDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ParkingRefundOrderMapper {

    /**
     * 根据id查找临停退费订单.
     *
     * @param id primary id
     * @return {@linkplain ParkingRefundOrderDO}
     */
    ParkingRefundOrderDO selectById(String id);

    /**
     * 新增临停退费订单.
     *
     * @param parkingRefundOrderDO {@linkplain ParkingRefundOrderDO}
     * @return int
     */
    int insert(ParkingRefundOrderDO parkingRefundOrderDO);

    /**
     * 更新临停退费订单.
     *
     * @param parkingRefundOrderDO {@linkplain ParkingRefundOrderDO}
     * @return int
     */
    int update(ParkingRefundOrderDO parkingRefundOrderDO);
}
