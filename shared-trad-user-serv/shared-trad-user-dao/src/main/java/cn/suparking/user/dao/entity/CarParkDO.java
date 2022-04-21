package cn.suparking.user.dao.entity;

import cn.suparking.common.api.configuration.SnowflakeConfig;
import cn.suparking.user.api.beans.CarParkDTO;
import cn.suparking.user.api.beans.UserWalletDTO;
import cn.suparking.user.api.enums.DeviceStatus;
import cn.suparking.user.api.enums.DeviceType;
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
public class CarParkDO extends BaseDO {

    private static final long serialVersionUID = -5015770782148938978L;

    private Long userId;

    private String parkNo;

    private String address;

    private String parkId;

    private Integer status;

    private Long deviceId;

    private Integer deviceStatus;

    private Integer deviceType;

    /**
     * build UserWalletDO.
     * @param carParkDTO {@linkplain UserWalletDTO}
     * @return {@link UserWalletDO}
     */
    public static CarParkDO buildCarParkDO(final CarParkDTO carParkDTO) {
        return Optional.ofNullable(carParkDTO).map(item -> {
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            CarParkDO carParkDO = CarParkDO.builder()
                    .userId(Long.valueOf(item.getUserId()))
                    .parkNo(item.getParkNo())
                    .address(item.getAddress())
                    .parkId(item.getParkId())
                    .status(item.getStatus())
                    .deviceId(Objects.nonNull(item.getDeviceId()) ? Long.valueOf(item.getDeviceId()) : null)
                    .deviceStatus(Objects.nonNull(item.getDeviceStatus()) ? item.getDeviceStatus() : DeviceStatus.UNKNOWN.getCode())
                    .deviceType(Objects.nonNull(item.getDeviceType()) ? item.getDeviceType() : DeviceType.UNKNOWN.getCode())
                    .build();

            if (Objects.isNull(item.getId())) {
                carParkDO.setId(SnowflakeConfig.snowflakeId());
                carParkDO.setDateCreated(currentTime);
            } else {
                carParkDO.setId(Long.valueOf(item.getId()));
                carParkDO.setDateUpdated(currentTime);
            }
            return carParkDO;
        }).orElse(null);
    }
}
