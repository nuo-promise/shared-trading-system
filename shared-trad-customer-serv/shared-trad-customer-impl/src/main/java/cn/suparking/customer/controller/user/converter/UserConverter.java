package cn.suparking.customer.controller.user.converter;

import cn.suparking.user.api.beans.UserDTO;
import cn.suparking.user.api.vo.UserVO;

public class UserConverter {

    /**
     * UserDTO converter UserVO.
     * @param userDTO {@link UserDTO}
     * @return {@link UserVO}
     */
    public static UserVO convertUserVO(final UserDTO userDTO) {
        return UserVO.builder()
                .iphone(userDTO.getIphone())
                .unionId(userDTO.getUnionId())
                .enabled(userDTO.getEnabled())
                .miniOpenId(userDTO.getMiniOpenId())
                .openId(userDTO.getOpenId())
                .registerType(userDTO.getRegisterType())
                .build();
    }
}
