package cn.suparking.invoice.service;

import api.beans.OtherOrderDTO;
import cn.suparking.order.entity.OtherOrderDO;

public interface OtherOrderService {
    /**
     * 根据订单id查询订单信息.
     *
     * @param id 订单id
     * @return OtherOrderDO {@linkplain OtherOrderDO}
     */
    OtherOrderDO findById(String id);

    /**
     * 创建或修改订单.
     *
     * @param otherOrderDTO 订单信息
     * @return Integer
     */
    Integer createOrUpdate(OtherOrderDTO otherOrderDTO);
}
