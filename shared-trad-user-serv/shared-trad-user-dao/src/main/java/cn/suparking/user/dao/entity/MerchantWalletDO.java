package cn.suparking.user.dao.entity;

import cn.suparking.common.api.configuration.SnowflakeConfig;
import cn.suparking.user.api.beans.MerchantWalletDTO;
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
public class MerchantWalletDO extends BaseDO {

    private static final long serialVersionUID = -2437868840527300537L;

    private Long userId;

    private Long merchantId;

    private Long amount;

    /**
     * build MerchantWalletDO.
     * @param merchantWalletDTO {@linkplain MerchantWalletDTO}
     * @return {@link MerchantWalletDO}
     */
    public static MerchantWalletDO buildMerchantWalletDO(final MerchantWalletDTO merchantWalletDTO) {
        return Optional.ofNullable(merchantWalletDTO).map(item -> {
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            MerchantWalletDO merchantWalletDO = MerchantWalletDO.builder()
                    .userId(Long.valueOf(item.getUserId()))
                    .merchantId(Long.valueOf(item.getMerchantId()))
                    .amount(item.getAmount())
                    .build();
            if (Objects.isNull(item.getId())) {
                merchantWalletDO.setId(SnowflakeConfig.snowflakeId());
                merchantWalletDO.setDateCreated(currentTime);
            } else {
                merchantWalletDO.setId(Long.valueOf(item.getId()));
                merchantWalletDO.setDateUpdated(currentTime);
            }
            return merchantWalletDO;
        }).orElse(null);
    }
}
