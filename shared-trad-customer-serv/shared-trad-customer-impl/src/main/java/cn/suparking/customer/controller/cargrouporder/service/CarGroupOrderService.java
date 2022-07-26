package cn.suparking.customer.controller.cargrouporder.service;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.customer.api.beans.cargroup.CarGroupDTO;
import cn.suparking.customer.api.beans.cargrouporder.CarGroupOrderDTO;
import cn.suparking.customer.api.beans.vip.VipPayDTO;
import cn.suparking.order.dao.entity.CarGroupOrderDO;

public interface CarGroupOrderService {
    /**
     * 新增/修改合约订单.
     *
     * @param carGroupOrderDTO 订单内容
     * @return SpkCommonResult {@linkplain SpkCommonResult}
     */
    SpkCommonResult createOrUpdate(CarGroupOrderDTO carGroupOrderDTO);

    /**
     * 组织合约订单数据.
     *
     * @param vipPayDTO {@linkplain VipPayDTO}
     * @param carGroup  {@linkplain CarGroupDTO}
     * @return {@link CarGroupOrderDTO}
     * @author ZDD
     * @date 2022/7/22 18:25:13
     */
    CarGroupOrderDTO makeCarGroupOrder(VipPayDTO vipPayDTO, CarGroupDTO carGroup, String orderState, String operateType);

    CarGroupOrderDO findByOrderNo(String orderNo);
}
