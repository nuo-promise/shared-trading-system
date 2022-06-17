package cn.suparking.customer.controller.cargrouporder.service;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.customer.api.beans.cargrouporder.CarGroupOrderDTO;

public interface CarGroupOrderService {
    /**
     * 新增/修改合约订单.
     *
     * @param carGroupOrderDTO 订单内容
     * @return SpkCommonResult {@linkplain SpkCommonResult}
     */
    SpkCommonResult createOrUpdate(CarGroupOrderDTO carGroupOrderDTO);
}
