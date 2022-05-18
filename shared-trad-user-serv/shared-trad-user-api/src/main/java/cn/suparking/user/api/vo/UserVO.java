package cn.suparking.user.api.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVO implements Serializable {

    private static final long serialVersionUID = 6138752221593750928L;

    /**
     * primary key.
     */
    private String id;

    /**
     * 用户名称.
     */
    private String userName;

    /**
     * 用户密码.
     */
    private String password;

    /**
     * 用户手机号码.
     */
    private String iphone;

    /**
     * 用户昵称.
     */
    private String nickName;

    private String miniOpenId;

    private String openId;

    private String unionId;

    /**
     * 用户状态 1:激活 2:未激活.
     */
    private Integer enabled;

    /**
     * 注册类型.
     */
    private Integer registerType;

    /**
     * 商户号.
     */
    private String merchantId;

    /**
     * created time.
     */
    private String dateCreated;

    /**
     * updated time.
     */
    private String dateUpdated;
}
