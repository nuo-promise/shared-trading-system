package cn.suparking.invoice.service;

import api.beans.ParkingRefundOrderDTO;
import cn.suparking.order.entity.ParkingRefundOrderDO;

public interface ParkingRefundOrderService {

    /**
     * 根据查询临停退费订单信息.
     *
     * @param id 计费ID
     * @return ParkingRefundOrderDO {@linkplain ParkingRefundOrderDO}
     */
    ParkingRefundOrderDO findById(String id);

    /**
     * 创建或修改临停退费订单.
     *
     * @param parkingRefundOrderDTO 临停订单退费信息
     * @return Integer
     */
    Integer createOrUpdate(ParkingRefundOrderDTO parkingRefundOrderDTO);
}
