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
public class UserWalletDTO implements Serializable {

    private static final long serialVersionUID = -5164270125594324820L;

    private String id;

    @NotNull
    @NotBlank
    private String userId;

    @NotNull
    private Long amount;
}
