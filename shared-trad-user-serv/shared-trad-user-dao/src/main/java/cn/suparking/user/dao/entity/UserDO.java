package cn.suparking.user.dao.entity;

import cn.suparking.common.api.utils.UUIDUtils;
import cn.suparking.user.api.beans.UserDTO;
import cn.suparking.user.api.enums.RegisterType;
import cn.suparking.user.api.enums.UserStatus;
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

    /**
     * 用户名称.
     */
    private String userName;

    /**
     * 用户密码.
     */
    private String password;

    /**
     * 用户手机号码.
     */
    private String iphone;

    /**
     * 用户昵称.
     */
    private String nickName;

    /**
     * 用户状态 1:激活 2:未激活
     */
    private Integer enabled;

    /**
     * 注册类型.
     */
    private Integer registerType;

    /**
     * 商户号.
     */
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
                    .enabled(item.getEnabled())
                    .registerType(item.getRegisterType())
                    .merchantId(item.getMerchantId())
                    .build();
            if (StringUtils.isEmpty(item.getId())) {
                userDO.setId(UUIDUtils.getInstance().generateShortUuid());
                userDO.setEnabled(UserStatus.ACTIVE.getCode());
                userDO.setDateCreated(currentTime);
            } else {
                userDO.setId(item.getId());
                userDO.setDateUpdated(currentTime);
                userDO.setEnabled(item.getEnabled());
            }
            return userDO;
        }).orElse(null);
    }
}
