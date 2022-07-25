package cn.suparking.customer.controller.user.service;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.user.api.beans.MiniRegisterDTO;
import cn.suparking.user.api.beans.UserDTO;
import cn.suparking.user.api.vo.UserVO;

public interface UserService {

    /**
     * register.
     * @param miniRegisterDTO {@link MiniRegisterDTO}
     * @return {@link UserVO}
     */
    UserVO register(MiniRegisterDTO miniRegisterDTO);

    /**
     * login.
     * @param code wx code
     * @return {@link UserVO}
     */
    UserVO login(String code);

    /**
     * 获取用户手机验证码.
     * @param sign {@link String}
     * @param phone {@link String}
     * @return {@link Boolean}
     */
    SpkCommonResult getPhoneCode(String sign, String phone);

    /**
     * 根据用户openId 更新用户信息.
     * @param sign {@link String}
     * @param userDTO  {@link UserDTO}
     * @return {@link SpkCommonResult}
     */
    SpkCommonResult changeUserPhone(String sign, UserDTO userDTO);
}
