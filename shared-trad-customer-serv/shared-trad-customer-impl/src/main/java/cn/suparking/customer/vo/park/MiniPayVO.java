package cn.suparking.customer.vo.park;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MiniPayVO {

    // 下单返回状态码.
    private String retCode;

    // 是否需要查询.
    private Boolean needQuery;

    // 吊起键盘的信息.
    private String payInfo;

    // 下单订单号.
    private String outTradeNo;

    // 平台
    private String platForm;

    // 优惠券锁定时间.
    private String discountDelayTime;

    private String type;
}
