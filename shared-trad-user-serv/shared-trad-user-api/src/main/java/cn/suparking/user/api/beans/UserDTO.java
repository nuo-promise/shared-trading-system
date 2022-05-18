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
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 3205442982068634056L;

    private String id;

    private String userName;

    private String password;

    @NotNull
    @NotBlank
    private String iphone;

    private String nickName;

    private String miniOpenId;

    private String openId;

    private String unionId;

    private Integer enabled;

    @NotNull
    private Integer registerType;

    private String merchantId;
}
