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
public class MerchantWalletDTO implements Serializable {

    private static final long serialVersionUID = 6133908542660582708L;

    private String id;

    @NotNull
    @NotBlank
    private String userId;

    private String merchantId;

    @NotNull
    private Long amount;
}
