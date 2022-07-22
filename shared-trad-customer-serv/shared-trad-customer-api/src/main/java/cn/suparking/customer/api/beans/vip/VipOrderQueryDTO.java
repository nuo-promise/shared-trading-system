package cn.suparking.customer.api.beans.vip;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VipOrderQueryDTO {
    // 订单号
    private String orderNo;

    // 支付类型
    private String payType;

    // 终端编号
    private String termNo;

    private Integer amount;

    // 支付平台方
    private String platForm;

    // 支付成功所需要的参数.
    private VipPayDTO vipPayDTO;

}
