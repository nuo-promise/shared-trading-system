package cn.suparking.order.dao.entity;

import cn.suparking.common.api.configuration.SnowflakeConfig;
import cn.suparking.order.api.beans.ChargeDetailDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChargeDetailDO implements Comparable<ChargeDetailDO> {

    private Long id;

    private Long changeInfoId;

    private String chargeTypeName;

    private Long beginTime;

    private Long endTime;

    private Integer parkingMinutes;

    private Integer balancedMinutes;

    private Integer freeMinutes;

    private Integer discountedMinutes;

    private Integer chargingMinutes;

    private Integer chargeAmount = 0;

    private String remark;

    /**
     * build ChargeDetailDO.
     * @param chargeDetailDTO {@link ChargeDetailDTO}
     * @return {@link ChargeDetailDO}
     */
    public static ChargeDetailDO buildChargeDetailDO(final ChargeDetailDTO chargeDetailDTO) {
        return Optional.ofNullable(chargeDetailDTO).map(item -> {
            ChargeDetailDO chargeDetailDO = ChargeDetailDO.builder()
                    .changeInfoId(Long.valueOf(item.getChangeInfoId()))
                    .chargeTypeName(item.getChargeTypeName())
                    .beginTime(item.getBeginTime())
                    .endTime(item.getEndTime())
                    .parkingMinutes(item.getParkingMinutes())
                    .balancedMinutes(item.getBalancedMinutes())
                    .freeMinutes(item.getFreeMinutes())
                    .discountedMinutes(item.getDiscountedMinutes())
                    .chargingMinutes(item.getChargingMinutes())
                    .chargeAmount(item.getChargeAmount())
                    .remark(item.getRemark())
                    .build();
            if (Objects.isNull(item.getId())) {
                chargeDetailDO.setId(SnowflakeConfig.snowflakeId());
            } else {
                chargeDetailDO.setId(Long.valueOf(item.getId()));
            }
            return chargeDetailDO;
        }).orElse(null);
    }

    @Override
    public int compareTo(final ChargeDetailDO o) {
        return (int) (o.endTime - this.endTime);
    }
}
