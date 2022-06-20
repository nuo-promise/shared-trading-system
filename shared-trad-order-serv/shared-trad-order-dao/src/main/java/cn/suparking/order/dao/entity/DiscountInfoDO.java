package cn.suparking.order.dao.entity;

import cn.suparking.common.api.configuration.SnowflakeConfig;
import cn.suparking.order.api.beans.DiscountInfoDTO;
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
public class DiscountInfoDO implements Serializable {

    private static final long serialVersionUID = -3980067719300176381L;

    private Long id;

    private Long parkingOrderId;

    private String discountNo;

    private String valueType;

    private Integer value;

    private Integer quantity;

    private String usedStartTime;

    private String usedEndTime;

    /**
     * build DiscountInfoDO.
     * @param discountInfoDTO {@link DiscountInfoDTO}
     * @return {@link DiscountInfoDO}
     */
    public static DiscountInfoDO buildDiscountInfoDO(final DiscountInfoDTO discountInfoDTO) {
        return Optional.ofNullable(discountInfoDTO).map(item -> {
            DiscountInfoDO discountInfoDO = DiscountInfoDO.builder()
                    .parkingOrderId(Long.valueOf(item.getParkingOrderId()))
                    .discountNo(item.getDiscountNo())
                    .valueType(item.getValueType())
                    .value(item.getValue())
                    .quantity(item.getQuantity())
                    .usedStartTime(item.getUsedStartTime())
                    .usedEndTime(item.getUsedEndTime())
                    .build();
            if (Objects.isNull(item.getId())) {
                discountInfoDO.setId(SnowflakeConfig.snowflakeId());
            } else {
                discountInfoDO.setId(Long.valueOf(item.getId()));
            }
            return discountInfoDO;
        }).orElse(null);
    }
}
