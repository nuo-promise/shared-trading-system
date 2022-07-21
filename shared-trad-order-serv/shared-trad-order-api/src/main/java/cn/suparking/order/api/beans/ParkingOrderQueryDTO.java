package cn.suparking.order.api.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkingOrderQueryDTO {
    // id
    private String id;

    // 页数
    private Integer page = 1;

    // 每页条数
    private Integer size = 20;

    // 车场编号
    private String projectNo;

    // 关键字--手机号
    private String keyword;

    // 出场应收的类型--1:大于0--2:等于0
    private Integer dueAmountType;

    // 出场实收的类型--1:大于应收--2:等于应收--3:小于应收
    private Integer receivedAmountType;

    // 查询入场时间ENTER/出场时间LEAVE -- 可以为空
    private String timeType;

    // 入场/出场 ======> 开始时间
    private String beginTime;

    // 入场/出场 ======> 结束时间
    private String endTime;

    // 会员车 / 临停车
    private Boolean tempType;

    // 车辆类别 ======> TEMP临停 / CONTRACTED合约 / SPECIAL特殊
    private String carTypeClass;

    // 车辆类型id
    private String carTypeId;

    // 支付方式
    private String payType;

    // 终端编号(和payType联合一起使用 900城市大脑 902ETC 903农行无感支付)
    private String termNo;

    // 标记异常原因
    private String comments;

    // 开票时间查询
    private Long invoiceBeginTime;

    // 开票时间查询
    private Long invoiceEndTime;

    // 最大可退金额
    private Integer maxRefundAmount;

    // 退费金额
    private Integer refundAmount;

    // 退费备注
    private String remark;

    // 现金退标记 on(现金退)  off(原路退回)
    private String refundMark;

    // 区域id
    private String subAreaId;

    //支付id
    private String payParkingId;

    //用户手机号
    private String phone;

    //用户id
    @NotNull
    @NotBlank
    private String userId;

    private Timestamp startDate;

    private Timestamp endDate;
}
