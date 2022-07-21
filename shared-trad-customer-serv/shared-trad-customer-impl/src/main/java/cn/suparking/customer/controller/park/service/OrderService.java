package cn.suparking.customer.controller.park.service;

import cn.suparking.customer.api.beans.discount.DiscountUsedDTO;
import cn.suparking.data.api.parkfee.Parking;
import cn.suparking.data.api.parkfee.ParkingOrder;

public interface OrderService {

    /**
     * 存储订单数据.
     * @param parkingOrder {@link ParkingOrder}
     * @param parking {@link Parking}
     * @param orderNo {@link String} 订单号
     * @param payType {@link String} 支付类型
     * @param termNo {@link String} 终端号
     * @param amount  {@link Integer} 支付金额
     * @param plateForm {@link String} 支付平台
     * @return {@link Boolean}
     */
    Boolean saveOrder(ParkingOrder parkingOrder, Parking parking, String orderNo, String payType,
                      String termNo, Integer amount, String plateForm);

    /**
     * 发送开闸指令.
     * @param deviceNo {@link String} 设备编号
     * @return {@link Boolean}
     */
    Boolean openCtpDevice(String deviceNo);


    /**
     * 核销优惠券.
     * @param discountUsedDTO {@link DiscountUsedDTO}
     * @return {@link Boolean}
     */
    Boolean discountUsed(DiscountUsedDTO discountUsedDTO);
}
