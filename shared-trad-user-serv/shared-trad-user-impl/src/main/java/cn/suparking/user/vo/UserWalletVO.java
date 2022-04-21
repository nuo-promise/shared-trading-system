package cn.suparking.user.vo;

import cn.suparking.common.api.utils.DateUtils;
import cn.suparking.user.dao.entity.UserWalletDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Optional;

/**
 * UserWallet VO.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserWalletVO implements Serializable {

    private static final long serialVersionUID = -6106240340505796843L;

    /**
     * primary key.
     */
    private String id;

    /**
     * user id.
     */
    private String userId;

    /**
     * user wallet amount.
     */
    private Long amount;

    /**
     * created time.
     */
    private String dateCreated;

    /**
     * updated time.
     */
    private String dateUpdated;

    /**
     * build UserWalletVO.
     *
     * @param userWalletDO {@linkplain UserWalletDO}
     * @return {@linkplain UserWalletVO}
     */
    public static UserWalletVO buildUserWalletVO(final UserWalletDO userWalletDO) {
        return Optional.ofNullable(userWalletDO)
                .map(item -> new UserWalletVO(item.getId().toString(), item.getUserId().toString(),
                        item.getAmount(),
                        DateUtils.localDateTimeToString(item.getDateCreated().toLocalDateTime()),
                        DateUtils.localDateTimeToString(item.getDateUpdated().toLocalDateTime())))
                .orElse(null);
    }
}
