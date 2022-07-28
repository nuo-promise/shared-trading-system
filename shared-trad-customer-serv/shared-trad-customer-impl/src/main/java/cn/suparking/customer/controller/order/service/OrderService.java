package cn.suparking.customer.controller.order.service;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.order.api.beans.ParkingOrderQueryDTO;

public interface OrderService {

    /**
     * 根据用户ID,获取用户信息.
     * @param parkingOrderQueryDTO {@link ParkingOrderQueryDTO}
     * @return {@link SpkCommonResult}
     */
    SpkCommonResult getLockOrder(ParkingOrderQueryDTO parkingOrderQueryDTO);
}
