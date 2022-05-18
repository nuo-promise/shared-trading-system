package cn.suparking.user.constant;

public class UserConstant {

    // =============================================== 常量 ===============================================

    /**
     * code 换取 session_key 时固定grant_type值.
     */
    public static final String GRANT_TYPE = "authorization_code";

    /**
     * 获取微信access_token.
     */
    public static final String ACCESS_TOKEN_GRANT_TYPE = "client_credential";

    public static final String WX_ACCESS_TOKEN_KEY = "WX_ACCESS_TOKEN";


    // =============================================== 路径 ===============================================

    /**
     * code 换取 session_key 接口地址.
     */
    public static final String JSCODE_TO_SESSION_URL = "https://api.weixin.qq.com/sns/jscode2session";

    /**
     * 获取 微信 access_token.
     */
    public static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";

    /**
     * 获取 getPhoneNumber.
     */
    public static final String PHONE_NUMBER_URL = "https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=";
}
