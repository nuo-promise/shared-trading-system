package cn.suparking.order.api.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkingOrderDTO implements Serializable {

    private static final long serialVersionUID = -8946257233700774353L;

    private String id;

    private String userId;

    @NotNull
    @NotBlank
    private String orderNo;

    private String payParkingId;

    private Integer tempType;

    private String carTypeClass;

    private String carTypeName;

    private String carTypeId;

    private Long beginTime;

    private Long endTime;

    private Long nextAggregateBeginTime;

    private Integer aggregatedMaxAmount;

    private Integer parkingMinutes;

    private Integer totalAmount;

    private Integer discountedMinutes;

    private Integer discountedAmount;

    private Integer chargeAmount;

    private Integer extraAmount;

    private Integer dueAmount;

    private Integer chargeDueAmount;

    private Integer paidAmount;

    private String payChannel;

    private String payType;

    private Long payTime;

    private Integer receivedAmount;

    private String termNo;

    private String operator;

    private Long expireTime;

    private String invoiceState;

    private String refundState;

    private String projectNo;

    private String creator;

    private String modifier;

    private Timestamp dateCreated;

    private Timestamp dateUpdated;
}
