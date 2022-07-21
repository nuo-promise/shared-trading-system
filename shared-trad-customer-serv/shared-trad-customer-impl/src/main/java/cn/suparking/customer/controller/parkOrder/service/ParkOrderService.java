package cn.suparking.customer.controller.parkOrder.service;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.order.api.beans.ParkingOrderQueryDTO;

public interface ParkOrderService {

    /**
     * 根据用户ID,获取用户信息.
     * @param parkingOrderQueryDTO {@link ParkingOrderQueryDTO}
     * @return {@link SpkCommonResult}
     */
    SpkCommonResult getLockOrder(ParkingOrderQueryDTO parkingOrderQueryDTO);
}
