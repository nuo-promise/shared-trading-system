package cn.suparking.order.entity;

import api.beans.ChargeInfoDTO;
import api.beans.ParkingRefundOrderDTO;
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
public class ParkingRefundOrderDO extends BaseDO {

    private static final long serialVersionUID = 5578766596603295006L;

    private Long userId;

    private String orderNo;

    private String payParkingId;

    private Integer maxRefundAmount;

    private Integer refundAmount;

    private String payChannel;

    private String payType;

    private String orderState;

    private String projectNo;

    private String creator;

    private String modifier;


    /**
     * build ParkingRefundOrderDO.
     * @param parkingRefundOrderDTO {@link ParkingRefundOrderDTO}
     * @return {@link ParkingRefundOrderDO}
     */
    public static ParkingRefundOrderDO buildParkingRefundOrderDO(final ParkingRefundOrderDTO parkingRefundOrderDTO) {
        return Optional.ofNullable(parkingRefundOrderDTO).map(item -> {
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            ParkingRefundOrderDO parkingRefundOrderDO = ParkingRefundOrderDO.builder()
                    .userId(Long.valueOf(item.getUserId()))
                    .orderNo(item.getOrderNo())
                    .payParkingId(item.getPayParkingId())
                    .maxRefundAmount(item.getMaxRefundAmount())
                    .refundAmount(item.getRefundAmount())
                    .payChannel(item.getPayChannel())
                    .payType(item.getPayType())
                    .orderState(item.getOrderState())
                    .projectNo(item.getProjectNo())
                    .creator(item.getCreator())
                    .modifier(item.getModifier())
                    .build();
            if (Objects.isNull(item.getId())) {
                parkingRefundOrderDO.setId(SnowflakeConfig.snowflakeId());
                parkingRefundOrderDO.setDateCreated(currentTime);
            } else {
                parkingRefundOrderDO.setId(Long.valueOf(item.getId()));
                parkingRefundOrderDO.setDateUpdated(currentTime);
            }
            return parkingRefundOrderDO;
        }).orElse(null);
    }
}
