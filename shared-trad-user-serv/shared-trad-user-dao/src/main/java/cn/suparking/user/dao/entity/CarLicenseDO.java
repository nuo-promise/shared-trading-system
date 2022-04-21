package cn.suparking.user.dao.entity;

import cn.suparking.common.api.configuration.SnowflakeConfig;
import cn.suparking.user.api.beans.CarLicenseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CarLicenseDO extends BaseDO {

    private static final long serialVersionUID = 285081783817625032L;

    private Long userId;

    private String carLicense;

    private Integer type;

    private String driverId;

    /**
     * build CarLicenseDO.
     * @param carLicenseDTO {@linkplain CarLicenseDTO}
     * @return {@link CarLicenseDO}
     */
    public static CarLicenseDO buildCarLicenseDO(final CarLicenseDTO carLicenseDTO) {
        return Optional.ofNullable(carLicenseDTO).map(item -> {
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            CarLicenseDO carLicenseDO = CarLicenseDO.builder()
                    .userId(Long.valueOf(item.getUserId()))
                    .carLicense(item.getCarLicense())
                    .type(item.getType())
                    .driverId(item.getDriverId())
                    .build();
            if (Objects.isNull(item.getId())) {
                carLicenseDO.setId(SnowflakeConfig.snowflakeId());
                carLicenseDO.setDateCreated(currentTime);
            } else {
                carLicenseDO.setId(Long.valueOf(item.getId()));
                carLicenseDO.setDateUpdated(currentTime);
            }
            return carLicenseDO;
        }).orElse(null);
    }
}
