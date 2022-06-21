package cn.suparking.customer.api.beans;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ParkBaseQueryDTO {
    /**
     * 用户手机号.
      */
    private String phone;

    /**
     * 用户在小程序的OpenID.
     */
    private String miniOpenId;

    /**
     * 用户在数泊平台关注的 unionId.
     */
    private String unionId;

    // 用户ID.
    private String userId;
}
