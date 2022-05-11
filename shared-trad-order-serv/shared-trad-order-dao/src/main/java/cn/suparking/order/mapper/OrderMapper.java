package cn.suparking.order.mapper;

import cn.suparking.order.entity.OrderDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单信息.
 */
@Mapper
public interface OrderMapper {
    /**
     * 根据id查找订单.
     *
     * @param id primary id
     * @return {@linkplain OrderDO}
     */
    OrderDO selectById(String id);

    /**
     * 新增订单信息.
     *
     * @param orderDO {@linkplain OrderDO}
     * @return int
     */
    int insert(OrderDO orderDO);

    /**
     * 更新订单信息.
     *
     * @param orderDO {@linkplain OrderDO}
     * @return int
     */
    int update(OrderDO orderDO);
}
