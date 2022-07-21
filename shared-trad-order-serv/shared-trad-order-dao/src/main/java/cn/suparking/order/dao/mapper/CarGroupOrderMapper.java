package cn.suparking.order.dao.mapper;

import cn.suparking.order.api.beans.CarGroupOrderQueryDTO;
import cn.suparking.order.dao.entity.CarGroupOrderDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CarGroupOrderMapper {

    /**
     * 合约订单总数.
     *
     * @param carGroupOrderQueryDTO {@link CarGroupOrderQueryDTO}
     * @return int
     */
    long listTotal(CarGroupOrderQueryDTO carGroupOrderQueryDTO);

    /**
     * 合约订单列表.
     *
     * @param carGroupOrderQueryDTO 订单信息
     * @return {@linkplain CarGroupOrderDO}
     */
    List<CarGroupOrderDO> list(CarGroupOrderQueryDTO carGroupOrderQueryDTO);

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
