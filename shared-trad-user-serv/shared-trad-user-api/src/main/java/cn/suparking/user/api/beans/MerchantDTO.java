package cn.suparking.user.api.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MerchantDTO implements Serializable {

    private static final long serialVersionUID = -6680149269445298364L;

    private String id;

    @NotNull
    @NotBlank
    private String merchantName;

    @NotNull
    @NotBlank
    private String iphone;

    private String merchantNumber;

    @NotNull
    @NotBlank
    private String cardId;

    private String businessLicenseId;

    @NotNull
    @NotBlank
    private String bankCard;

    @NotNull
    private Integer bankCardType;

    @NotNull
    @NotBlank
    private String address;

    private String password;

    @NotNull
    private Integer enabled;

    private String creator;

    private String modify;
}
