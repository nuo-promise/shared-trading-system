package cn.suparking.order.dao.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LockOrderVO {

    private String projectNo;

    private String parkName;

    private String status;

    private String address;

    private Double latitude;

    private Double longitude;

    private String minutes;

    private Integer dueAmount;

    // 开始结束时间
    private String time;

    // 支付时间
    private String payTime;

    private String invoiceState;

    private String refundState;
}
