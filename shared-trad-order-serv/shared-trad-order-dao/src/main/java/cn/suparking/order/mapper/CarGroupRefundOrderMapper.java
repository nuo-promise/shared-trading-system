package cn.suparking.order.mapper;

import cn.suparking.order.entity.CarGroupRefundOrderDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CarGroupRefundOrderMapper {

    /**
     * 根据id查找合约退费订单.
     *
     * @param id primary id
     * @return {@linkplain CarGroupRefundOrderDO}
     */
    CarGroupRefundOrderDO selectById(String id);

    /**
     * 新增合约退费订单信息.
     *
     * @param carGroupRefundOrderDO {@linkplain CarGroupRefundOrderDO}
     * @return int
     */
    int insert(CarGroupRefundOrderDO carGroupRefundOrderDO);

    /**
     * 更新合约订单信息.
     *
     * @param carGroupRefundOrderDO {@linkplain CarGroupRefundOrderDO}
     * @return int
     */
    int update(CarGroupRefundOrderDO carGroupRefundOrderDO);
}
