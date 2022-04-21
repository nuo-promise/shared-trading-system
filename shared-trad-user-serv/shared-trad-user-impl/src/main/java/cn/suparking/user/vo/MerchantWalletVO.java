package cn.suparking.user.vo;

import cn.suparking.common.api.utils.DateUtils;
import cn.suparking.user.dao.entity.MerchantWalletDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MerchantWalletVO implements Serializable {

    private static final long serialVersionUID = -6397769389683213433L;

    /**
     * primary id.
     */
    private String id;

    /**
     * user id.
     */
    private String userId;

    /**
     * merchant id.
     */
    private String merchantId;

    /**
     * merchant amount.
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
     * build MerchantWalletVO.
     *
     * @param merchantWalletDO {@linkplain MerchantWalletDO}
     * @return {@linkplain UserVO}
     */
    public static MerchantWalletVO buildMerchantWalletVO(final MerchantWalletDO merchantWalletDO) {
        return Optional.ofNullable(merchantWalletDO)
                .map(item -> new MerchantWalletVO(item.getId().toString(), item.getUserId().toString(), item.getMerchantId().toString(),
                        item.getAmount(),
                        DateUtils.localDateTimeToString(item.getDateCreated().toLocalDateTime()),
                        DateUtils.localDateTimeToString(item.getDateUpdated().toLocalDateTime())))
                .orElse(null);
    }
}
