package cn.suparking.data.api.parkfee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkingOrder {

    private String orderNo;

    private String payParkingId;

    private Long userId;

    private Boolean tempType;

    private String carTypeClass;

    private String carTypeName;

    private String carTypeId;

    private Long beginTime;

    private Long endTime;

    private Long nextAggregateBeginTime;

    private Integer aggregatedMaxAmount;

    private Long parkingMinutes;

    private DiscountInfo discountInfo;

    private LinkedList<ChargeInfo> chargeInfos;

    private Integer totalAmount;

    private Integer discountedMinutes;

    private Integer discountedAmount;

    private HistoryOrderTraceInfo historyOrderTraceInfo;

    private Integer chargeAmount;

    private Integer extraAmount;

    private Integer dueAmount;

    // 实际计算金额
    private Integer chargeDueAmount;

    // 预付金额
    private Integer paidAmount;

    private String payChannel;

    private String payType;

    private Long payTime;

    private Integer receivedAmount;

    private String termNo;

    private String operator;

    private Long bestBefore;

    // expireTime 等拿到计费信息等待支付时候赋值
    private Long expireTime;

    private String remark;

    private String comments;

    private String invoiceState;

    private String refundState;

    private String status;

    private String projectNo;

    private String creator;

    private Long createTime;

    private String modifier;

    private Long modifyTime;
}
