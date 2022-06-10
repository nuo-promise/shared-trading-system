package cn.suparking.order.dao.entity;

import cn.suparking.order.api.beans.ChargeInfoDTO;
import cn.suparking.common.api.configuration.SnowflakeConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChargeInfoDO implements Serializable {

    private static final long serialVersionUID = 9104829773833342278L;

    private Long id;

    private Long parkingOrderId;

    private Integer beginCycleSeq;

    private Integer cycleNumber;

    private Integer parkingMinutes;

    private Integer balancedMinutes;

    private Integer discountedMinutes;

    private Integer totalAmount;

    private Integer extraAmount;

    /**
     * build ChargeInfoDO.
     * @param chargeInfoDTO {@link ChargeInfoDO}
     * @return {@link ChargeInfoDO}
     */
    public static ChargeInfoDO buildChargeInfoDO(final ChargeInfoDTO chargeInfoDTO) {
        return Optional.ofNullable(chargeInfoDTO).map(item -> {
            ChargeInfoDO chargeInfoDO = ChargeInfoDO.builder()
                    .parkingOrderId(Long.valueOf(item.getParkingOrderId()))
                    .beginCycleSeq(item.getBeginCycleSeq())
                    .cycleNumber(item.getCycleNumber())
                    .parkingMinutes(item.getParkingMinutes())
                    .balancedMinutes(item.getBalancedMinutes())
                    .discountedMinutes(item.getDiscountedMinutes())
                    .totalAmount(item.getTotalAmount())
                    .extraAmount(item.getExtraAmount())
                    .build();
            if (Objects.isNull(item.getId())) {
                chargeInfoDO.setId(SnowflakeConfig.snowflakeId());
            } else {
                chargeInfoDO.setId(Long.valueOf(item.getId()));
            }
            return chargeInfoDO;
        }).orElse(null);
    }
}
