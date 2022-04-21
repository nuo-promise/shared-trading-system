package cn.suparking.user.dao.entity;

import cn.suparking.common.api.configuration.SnowflakeConfig;
import cn.suparking.common.api.utils.UUIDUtils;
import cn.suparking.user.api.beans.MerchantDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MerchantDO extends BaseDO {

    private static final long serialVersionUID = -8791618690939817561L;

    private String merchantName;

    private String iphone;

    private String merchantNumber;

    private String cardId;

    private String businessLicenseId;

    private String bankCard;

    private Integer bankCardType;

    private String address;

    private String password;

    private int enabled;

    private String creator;

    private String modify;

    /**
     * build MerchantDO.
     * @param merchantDTO {@linkplain MerchantDTO}
     * @return {@link MerchantDO}
     */
    public static MerchantDO buildMerchantDO(final MerchantDTO merchantDTO) {
        return Optional.ofNullable(merchantDTO).map(item -> {
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            MerchantDO merchantDO = MerchantDO.builder()
                    .merchantName(item.getMerchantName())
                    .iphone(item.getIphone())
                    .merchantNumber(StringUtils.isNotBlank(item.getMerchantNumber()) ? item.getMerchantNumber() : UUIDUtils.getInstance().generateShortUuid())
                    .cardId(item.getCardId())
                    .businessLicenseId(item.getBusinessLicenseId())
                    .bankCard(item.getBankCard())
                    .bankCardType(item.getBankCardType())
                    .address(item.getAddress())
                    .password(item.getPassword())
                    .enabled(item.getEnabled())
                    .creator(item.getCreator())
                    .modify(StringUtils.isNotBlank(item.getModify()) ? item.getModify() : item.getCreator())
                    .build();
            if (Objects.isNull(item.getId())) {
                merchantDO.setId(SnowflakeConfig.snowflakeId());
                merchantDO.setDateCreated(currentTime);
            } else {
                merchantDO.setId(Long.valueOf(item.getId()));
                merchantDO.setDateUpdated(currentTime);
            }
            return merchantDO;
        }).orElse(null);
    }
}
