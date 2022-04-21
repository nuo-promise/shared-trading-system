package cn.suparking.user.vo;

import cn.suparking.common.api.utils.DateUtils;
import cn.suparking.user.dao.entity.CarParkDO;
import cn.suparking.user.dao.entity.UserWalletDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

/**
 * CarPark VO.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarParkVO implements Serializable {

    private static final long serialVersionUID = 2169011840517851125L;

    /**
     * primary key.
     */
    private String id;

    /**
     * user primary key.
     */
    private String userId;

    /**
     * park number.
     */
    private String parkNo;


    /**
     * car address.
     */
    private String address;

    /**
     * car pic.
     */
    private String parkId;

    /**
     * car park status.
     * 1 free 2 reversing 3 reversed 4 entered 5 left
     */
    private Integer status;

    /**
     * device id.
     */
    private String deviceId;

    /**
     * device status.
     * 1. free 2 busy
     */
    private Integer deviceStatus;

    /**
     * device type.
     * 1 gero 2 lock
     */
    private Integer deviceType;

    /**
     * created time.
     */
    private String dateCreated;

    /**
     * updated time.
     */
    private String dateUpdated;

    /**
     * build CarParkDO.
     *
     * @param carParkDO {@linkplain CarParkDO}
     * @return {@linkplain CarParkVO}
     */
    public static CarParkVO buildCarParkVO(final CarParkDO carParkDO) {
        return Optional.ofNullable(carParkDO)
                .map(item -> new CarParkVO(item.getId().toString(), item.getUserId().toString(),
                        item.getParkNo(), item.getAddress(), item.getParkId(), item.getStatus(),
                        Objects.nonNull(item.getDeviceId()) ? item.getDeviceId().toString() : null,
                        item.getDeviceStatus(), item.getDeviceType(),
                        DateUtils.localDateTimeToString(item.getDateCreated().toLocalDateTime()),
                        DateUtils.localDateTimeToString(item.getDateUpdated().toLocalDateTime())))
                .orElse(null);
    }
}
