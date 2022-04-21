package cn.suparking.user.dao.entity;

import cn.suparking.common.api.configuration.SnowflakeConfig;
import cn.suparking.user.api.beans.UserWalletDTO;
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
public class UserWalletDO extends BaseDO {

    private static final long serialVersionUID = 1997305691488473345L;

    private Long userId;

    private Long amount;

    /**
     * build UserWalletDO.
     * @param userWalletDTO {@linkplain UserWalletDTO}
     * @return {@link UserWalletDO}
     */
    public static UserWalletDO buildUserWalletDO(final UserWalletDTO userWalletDTO) {
        return Optional.ofNullable(userWalletDTO).map(item -> {
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            UserWalletDO userWalletDO = UserWalletDO.builder()
                    .userId(Long.valueOf(item.getUserId()))
                    .amount(item.getAmount())
                    .build();
            if (Objects.isNull(item.getId())) {
                userWalletDO.setId(SnowflakeConfig.snowflakeId());
                userWalletDO.setDateCreated(currentTime);
            } else {
                userWalletDO.setId(Long.valueOf(item.getId()));
                userWalletDO.setDateUpdated(currentTime);
            }
            return userWalletDO;
        }).orElse(null);
    }
}
