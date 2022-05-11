package cn.suparking.invoice.service;

import api.beans.OrderDTO;
import cn.suparking.order.entity.OrderDO;

public interface OrderService {
    /**
     * 根据订单id查询订单信息.
     *
     * @param id 订单id
     * @return OrderDO {@linkplain OrderDO}
     */
    OrderDO findById(String id);

    /**
     * 创建或修改订单.
     *
     * @param orderDTO 订单信息
     * @return Integer
     */
    Integer createOrUpdate(OrderDTO orderDTO);
}
