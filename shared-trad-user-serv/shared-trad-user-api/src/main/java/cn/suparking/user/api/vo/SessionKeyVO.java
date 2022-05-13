package cn.suparking.user.api.vo;

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
public class SessionKeyVO implements Serializable {

    private static final long serialVersionUID = -4952795851464885155L;

    /**
     * 用户唯一标识.
     */
    @NotNull
    private String openid;

    /**
     * 会话密钥.
     */
    private String sessionKey;

    /**
     * 用户在开放平台的唯一标识符.
     */
    private String unionid;
}
