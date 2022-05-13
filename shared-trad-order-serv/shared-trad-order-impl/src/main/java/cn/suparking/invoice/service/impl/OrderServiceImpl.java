package cn.suparking.invoice.service.impl;

import api.beans.OrderDTO;
import cn.suparking.invoice.service.OrderService;
import cn.suparking.order.entity.OrderDO;
import cn.suparking.order.mapper.OrderMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;

    public OrderServiceImpl(final OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    /**
     * 根据订单id查询订单信息.
     *
     * @param id 订单id
     * @return OrderDO {@linkplain OrderDO}
     */
    @Override
    public OrderDO findById(final String id) {
        return orderMapper.selectById(id);
    }

    /**
     * 创建或修改订单.
     *
     * @param orderDTO 订单信息
     * @return Integer
     */
    @Override
    public Integer createOrUpdate(OrderDTO orderDTO) {
        OrderDO orderDO = OrderDO.buildOrderDO(orderDTO);
        if (StringUtils.isEmpty(orderDTO.getId())) {
            return orderMapper.insert(orderDO);
        } else {
            return orderMapper.update(orderDO);
        }
    }
}
