package cn.suparking.order.dao.mapper;

import cn.suparking.order.dao.entity.CarGroupOrderDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CarGroupOrderMapper {

    /**
     * 根据id查找合约订单.
     *
     * @param id primary id
     * @return {@linkplain CarGroupOrderDO}
     */
    CarGroupOrderDO selectById(String id);

    /**
     * 新增合约订单信息.
     *
     * @param carGroupOrderDO {@linkplain CarGroupOrderDO}
     * @return int
     */
    int insert(CarGroupOrderDO carGroupOrderDO);

    /**
     * 更新合约订单信息.
     *
     * @param carGroupOrderDO {@linkplain CarGroupOrderDO}
     * @return int
     */
    int update(CarGroupOrderDO carGroupOrderDO);
}
