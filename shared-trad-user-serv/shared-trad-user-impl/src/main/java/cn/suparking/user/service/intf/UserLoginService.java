package cn.suparking.user.service.intf;

import cn.suparking.user.api.vo.PhoneInfoVO;
import cn.suparking.user.api.vo.RegisterVO;
import cn.suparking.user.api.vo.SessionVO;

public interface UserLoginService {

    /**
     * 根据code获取openId和sessionKey.
     *
     * @param code wx login code
     * @return {@linkplain RegisterVO}
     */
    SessionVO getSessionKey(String code);

    /**
     * get wx accessToken.
     */
    void getAccessToken();


    /**
     * get user phone.
     * @param phoneCode phone
     * @return {@link PhoneInfoVO}
     */
    PhoneInfoVO getPhoneInfo(String phoneCode);
}
