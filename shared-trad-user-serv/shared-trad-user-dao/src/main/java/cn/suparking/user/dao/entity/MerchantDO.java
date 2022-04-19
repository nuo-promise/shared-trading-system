package cn.suparking.user.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MerchantDO extends BaseDO {

    private String merchantName;

    private String iphone;

    private String merchantNumber;

    private String cardId;

    private String businessLicenseId;

    private String bankCard;

    private String bankCardType;

    private String address;

    private String password;

    private int enabled;

    private String creator;

    private String modify;
}
