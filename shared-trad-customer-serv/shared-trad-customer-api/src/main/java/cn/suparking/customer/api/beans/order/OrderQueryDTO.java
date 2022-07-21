package cn.suparking.customer.api.beans.order;

import cn.suparking.data.api.parkfee.Parking;
import cn.suparking.data.api.parkfee.ParkingOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderQueryDTO {

    // 订单号
    private String orderNo;

    // 支付类型
    private String payType;

    // 终端编号
    private String termNo;

    private Integer amount;

    // 支付平台方
    private String platForm;

    // 是否存在优惠券
    private String discountNo;

    private Parking parking;

    private ParkingOrder parkingOrder;
}
