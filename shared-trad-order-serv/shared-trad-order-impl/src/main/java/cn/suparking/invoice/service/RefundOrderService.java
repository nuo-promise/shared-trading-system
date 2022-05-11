package cn.suparking.invoice.service;

import api.beans.RefundOrderDTO;
import cn.suparking.order.entity.RefundOrderDO;

public interface RefundOrderService {
    /**
     * 根据订单id查询订单信息.
     *
     * @param id 订单id
     * @return RefundOrderDO {@linkplain RefundOrderDO}
     */
    RefundOrderDO findById(String id);

    /**
     * 创建或修改订单.
     *
     * @param refundOrderDTO 订单信息
     * @return Integer
     */
    Integer createOrUpdate(RefundOrderDTO refundOrderDTO);
}
