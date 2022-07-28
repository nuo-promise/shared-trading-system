package cn.suparking.customer.controller.cargrouporder.service;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.customer.api.beans.cargroup.CarGroupDTO;
import cn.suparking.customer.api.beans.vip.VipPayDTO;
import cn.suparking.order.api.beans.CarGroupOrderDTO;
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
     * @param orderState 订单状态
     * @param operateType 操作类型 新办 NEW 续费 RENEW
     * @return {@link CarGroupOrderDTO}
     * @author ZDD
     * @date 2022/7/22 18:25:13
     */
    CarGroupOrderDTO makeCarGroupOrder(VipPayDTO vipPayDTO, CarGroupDTO carGroup, String orderState, String operateType);

    /**
     * 根据订单号查询订单信息.
     * @param orderNo 订单号
     * @return {@link CarGroupOrderDO}
     */
    CarGroupOrderDO findByOrderNo(String orderNo);
}
