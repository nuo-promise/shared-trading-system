package cn.suparking.invoice.service;

import api.beans.ParkingOrderDTO;
import cn.suparking.order.entity.ParkingOrderDO;

public interface ParkingOrderService {

    /**
     * 根据查询临停订单信息.
     *
     * @param id 计费ID
     * @return ParkingOrderDO {@linkplain ParkingOrderDO}
     */
    ParkingOrderDO findById(String id);

    /**
     * 创建或修改临停订单.
     *
     * @param parkingOrderDTO 临停订单信息
     * @return Integer
     */
    Integer createOrUpdate(ParkingOrderDTO parkingOrderDTO);
}
