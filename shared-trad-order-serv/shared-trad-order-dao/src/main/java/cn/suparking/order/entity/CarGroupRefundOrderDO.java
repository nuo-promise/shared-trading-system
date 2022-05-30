package cn.suparking.order.entity;

import api.beans.CarGroupRefundOrderDTO;
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
public class CarGroupRefundOrderDO extends BaseDO {

    private static final long serialVersionUID = -8727132105970628203L;

    private Long userId;

    private String orderNo;

    private String payOrderNo;

    private Long carGroupId;

    private String carTypeId;

    private String carTypeName;

    private String protocolId;

    private String protocolName;

    private Long beginDate;

    private Long endDate;

    private Integer maxRefundAmount;

    private Integer refundAmount;

    private String payChannel;

    private String payType;

    private String userName;

    private String userMobile;

    private String orderState;

    private String projectNo;

    private String creator;

    private String modifier;

    /**
     * build CarGroupRefundOrderDO.
     * @param carGroupRefundOrderDTO {@link CarGroupRefundOrderDTO}
     * @return {@link CarGroupRefundOrderDO}
     */
    public static CarGroupRefundOrderDO buildCarGroupRefundOrderDO(final CarGroupRefundOrderDTO carGroupRefundOrderDTO) {
        return Optional.ofNullable(carGroupRefundOrderDTO).map(item -> {
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            CarGroupRefundOrderDO carGroupRefundOrderDO = CarGroupRefundOrderDO.builder()
                    .userId(Long.valueOf(item.getUserId()))
                    .orderNo(item.getOrderNo())
                    .payOrderNo(item.getPayOrderNo())
                    .carGroupId(Long.valueOf(item.getCarGroupId()))
                    .carTypeId(item.getCarTypeId())
                    .carTypeName(item.getCarTypeName())
                    .protocolId(item.getProtocolId())
                    .protocolName(item.getProtocolName())
                    .beginDate(item.getBeginDate())
                    .endDate(item.getEndDate())
                    .maxRefundAmount(item.getMaxRefundAmount())
                    .refundAmount(item.getRefundAmount())
                    .payChannel(item.getPayChannel())
                    .payType(item.getPayType())
                    .userName(item.getUserName())
                    .userMobile(item.getUserMobile())
                    .orderState(item.getOrderState())
                    .projectNo(item.getProjectNo())
                    .creator(item.getCreator())
                    .modifier(item.getModifier())
                    .build();
            if (Objects.isNull(item.getId())) {
                carGroupRefundOrderDO.setId(SnowflakeConfig.snowflakeId());
                carGroupRefundOrderDO.setDateCreated(currentTime);
            } else {
                carGroupRefundOrderDO.setId(Long.valueOf(item.getId()));
                carGroupRefundOrderDO.setDateUpdated(currentTime);
            }
            return carGroupRefundOrderDO;
        }).orElse(null);
    }
}
