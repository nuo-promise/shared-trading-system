package cn.suparking.user.dao.entity;

import cn.suparking.common.api.utils.UUIDUtils;
import cn.suparking.user.api.beans.UserDTO;
import cn.suparking.user.api.enums.RegisterType;
import cn.suparking.user.api.enums.UserStatus;
import cn.suparking.user.dao.converter.RegisterTypeConverter;
import cn.suparking.user.dao.converter.UserStatusConverter;
import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import java.sql.Timestamp;
import java.util.Optional;

/**
 * user table entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public final class UserDO extends BaseDO {

    private String userName;

    private String password;

    private String iphone;

    private String nickName;

    private UserStatus enabled;

    private RegisterType registerType;

    private String merchantId;

    /**
     * build userDO.
     * @param userDTO {@linkplain UserDTO}
     * @return {@link UserDO}
     */
    public static UserDO buildUserDO(final UserDTO userDTO) {
        return Optional.ofNullable(userDTO).map(item -> {
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            UserDO userDO = UserDO.builder()
                    .userName(item.getUserName())
                    .password(item.getPassword())
                    .iphone(item.getIphone())
                    .nickName(item.getNickName())
                    .enabled(UserStatus.convert(item.getEnabled()))
                    .registerType(RegisterType.convert(item.getRegisterType()))
                    .merchantId(item.getMerchantId())
                    .build();
            if (StringUtils.isEmpty(item.getId())) {
                userDO.setId(UUIDUtils.getInstance().generateShortUuid());
                userDO.setEnabled(UserStatus.ACTIVE);
                userDO.setDateCreated(currentTime);
            } else {
                userDO.setId(item.getId());
                userDO.setDateUpdated(currentTime);
                userDO.setEnabled(UserStatus.convert(item.getEnabled()));
            }
            return userDO;
        }).orElse(null);
    }
}
