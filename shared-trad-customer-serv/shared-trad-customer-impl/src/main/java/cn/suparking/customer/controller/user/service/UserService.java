package cn.suparking.customer.controller.user.service;

import cn.suparking.user.api.beans.MiniRegisterDTO;
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

}
