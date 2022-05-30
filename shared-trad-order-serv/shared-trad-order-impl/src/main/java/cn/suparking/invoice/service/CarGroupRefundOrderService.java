package cn.suparking.invoice.service;

import api.beans.CarGroupRefundOrderDTO;
import cn.suparking.order.entity.CarGroupRefundOrderDO;

public interface CarGroupRefundOrderService {

    /**
     * 根据合约id查询合约退费信息.
     *
     * @param id 合约id
     * @return CarGroupRefundOrderDO {@linkplain CarGroupRefundOrderDO}
     */
    CarGroupRefundOrderDO findById(String id);

    /**
     * 创建或修改合约退费订单.
     *
     * @param carGroupRefundOrderDTO 退费订单信息
     * @return Integer
     */
    Integer createOrUpdate(CarGroupRefundOrderDTO carGroupRefundOrderDTO);
}
