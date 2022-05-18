package cn.suparking.user.api.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MiniRegisterDTO implements Serializable {

    private static final long serialVersionUID = 3159952960557190780L;

    /**
     * 微信端返回code，使用code 换取 openId unionId.
     */
    @NotNull
    private String code;

    @NotNull
    private String phoneCode;
}
