package cn.suparking.user.vo;

import cn.suparking.common.api.utils.DateUtils;
import cn.suparking.user.dao.entity.CarLicenseDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarLicenseVO implements Serializable {

    private static final long serialVersionUID = -553087946524008929L;

    /**
     * primary id.
     */
    private String id;

    /**
     * user id.
     */
    private String userId;

    /**
     * carLicense.
     */
    private String carLicense;

    /**
     * car license is default.
     */
    private Integer type;

    /**
     * car path.
     */
    private String driverId;

    /**
     * created time.
     */
    private String dateCreated;

    /**
     * updated time.
     */
    private String dateUpdated;

    /**
     * build CarLicenseVO.
     *
     * @param carLicenseDO {@linkplain CarLicenseDO}
     * @return {@linkplain CarLicenseVO}
     */
    public static CarLicenseVO buildCarLicenseVO(final CarLicenseDO carLicenseDO) {
        return Optional.ofNullable(carLicenseDO)
                .map(item -> new CarLicenseVO(item.getId().toString(), item.getUserId().toString(), item.getCarLicense(),
                        item.getType(), StringUtils.isNotBlank(item.getDriverId()) ? item.getDriverId() : null,
                        DateUtils.localDateTimeToString(item.getDateCreated().toLocalDateTime()),
                        DateUtils.localDateTimeToString(item.getDateUpdated().toLocalDateTime())))
                .orElse(null);
    }
}
