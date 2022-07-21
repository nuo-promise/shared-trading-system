package cn.suparking.order.dao.entity;

import cn.suparking.order.api.beans.ParkingOrderDTO;
import cn.suparking.common.api.configuration.SnowflakeConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ParkingOrderDO extends BaseDO {

    private static final long serialVersionUID = -3064987299814485320L;

    private Long userId;

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

    private Timestamp invoiceTime;

    private String refundState;

    private String projectNo;

    private String status;

    private String creator;

    private String modifier;

    /**
     * build ParkingOrderDO.
     * @param parkingOrderDTO {@link ParkingOrderDTO}
     * @return {@link ParkingOrderDO}
     */
    public static ParkingOrderDO buildParkingOrderDO(final ParkingOrderDTO parkingOrderDTO) {
        return Optional.ofNullable(parkingOrderDTO).map(item -> {
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            ParkingOrderDO parkingOrderDO = ParkingOrderDO.builder()
                    .userId(Long.valueOf(item.getUserId()))
                    .orderNo(item.getOrderNo())
                    .payParkingId(item.getPayParkingId())
                    .tempType(item.getTempType())
                    .carTypeClass(item.getCarTypeClass())
                    .carTypeName(item.getCarTypeName())
                    .carTypeId(item.getCarTypeId())
                    .beginTime(item.getBeginTime())
                    .endTime(item.getEndTime())
                    .nextAggregateBeginTime(item.getNextAggregateBeginTime())
                    .aggregatedMaxAmount(item.getAggregatedMaxAmount())
                    .parkingMinutes(Math.toIntExact(item.getParkingMinutes()))
                    .totalAmount(item.getTotalAmount())
                    .discountedMinutes(item.getDiscountedMinutes())
                    .discountedAmount(item.getDiscountedAmount())
                    .chargeAmount(item.getChargeAmount())
                    .extraAmount(item.getExtraAmount())
                    .dueAmount(item.getDueAmount())
                    .chargeDueAmount(item.getChargeDueAmount())
                    .paidAmount(item.getPaidAmount())
                    .payChannel(item.getPayChannel())
                    .payType(item.getPayType())
                    .payTime(item.getPayTime())
                    .receivedAmount(item.getReceivedAmount())
                    .termNo(item.getTermNo())
                    .operator(item.getOperator())
                    .expireTime(item.getExpireTime())
                    .invoiceState(item.getInvoiceState())
                    .invoiceTime(item.getInvoiceTime())
                    .refundState(item.getRefundState())
                    .status(item.getStatus())
                    .projectNo(item.getProjectNo())
                    .creator(item.getCreator())
                    .modifier(item.getModifier())
                    .build();
            if (Objects.isNull(item.getId())) {
                parkingOrderDO.setId(SnowflakeConfig.snowflakeId());
                parkingOrderDO.setDateCreated(currentTime);
            } else {
                parkingOrderDO.setId(Long.valueOf(item.getId()));
                parkingOrderDO.setDateUpdated(currentTime);
            }
            return parkingOrderDO;
        }).orElse(null);
    }
}
