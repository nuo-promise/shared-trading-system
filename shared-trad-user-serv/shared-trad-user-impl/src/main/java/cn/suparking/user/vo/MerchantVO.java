package cn.suparking.user.vo;

import cn.suparking.common.api.utils.DateUtils;
import cn.suparking.user.dao.entity.MerchantDO;
import cn.suparking.user.dao.entity.UserDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MerchantVO implements Serializable {

    private static final long serialVersionUID = 8747346015674424813L;

    /**
     * primary id.
     */
    private String id;

    /**
     * merchant Name.
     */
    private String merchantName;

    /**
     * merchant iphone.
     */
    private String iphone;

    /**
     * merchant number.
     */
    private String merchantNumber;

    /**
     * card path.
     */
    private String cardId;

    /**
     * business license id.
     */
    private String businessLicenseId;

    /**
     * bank card.
     */
    private String bankCard;

    /**
     * bank card type.
     */
    private Integer bankCardType;

    /**
     * merchant address.
     */
    private String address;

    /**
     * merchant password.
     */
    private String password;

    /**
     * status.
     */
    private Integer enabled;

    /**
     * creator.
     */
    private String creator;

    /**
     * modify.
     */
    private String modify;

    /**
     * created time.
     */
    private String dateCreated;

    /**
     * updated time.
     */
    private String dateUpdated;

    /**
     * build MerchantVO.
     *
     * @param merchantDO {@linkplain MerchantDO}
     * @return {@linkplain MerchantVO}
     */
    public static MerchantVO buildMerchantVO(final MerchantDO merchantDO) {
        return Optional.ofNullable(merchantDO)
                .map(item -> new MerchantVO(item.getId().toString(), item.getMerchantName(), item.getIphone(), item.getMerchantNumber(),
                        item.getCardId(), item.getBusinessLicenseId(), item.getBankCard(), item.getBankCardType(),
                        item.getAddress(), item.getPassword(), item.getEnabled(),
                        item.getCreator(), item.getModify(),
                        DateUtils.localDateTimeToString(item.getDateCreated().toLocalDateTime()),
                        DateUtils.localDateTimeToString(item.getDateUpdated().toLocalDateTime())))
                .orElse(null);
    }
}
