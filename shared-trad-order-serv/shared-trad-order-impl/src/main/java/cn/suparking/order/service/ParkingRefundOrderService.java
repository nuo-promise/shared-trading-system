package cn.suparking.order.service;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.order.api.beans.ParkingRefundOrderDTO;
import cn.suparking.order.api.beans.ParkingRefundOrderQueryDTO;
import cn.suparking.order.dao.entity.ParkingRefundOrderDO;

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

    /**
     * 根据原支付订单号获取数据.
     * @param parkingRefundOrderQueryDTO
     * @return cn.suparking.common.api.beans.SpkCommonResult
     * @author ZDD
     * @date 2022/7/18 16:27:55
     */
    SpkCommonResult getParkingRefundOrderByPayOrderNO(ParkingRefundOrderQueryDTO parkingRefundOrderQueryDTO);
}
