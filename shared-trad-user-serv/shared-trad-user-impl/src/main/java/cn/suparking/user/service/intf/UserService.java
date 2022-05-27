package cn.suparking.user.service.intf;

import cn.suparking.user.api.beans.UserDTO;
import cn.suparking.user.vo.UserVO;

public interface UserService {

    /**
     * create or update dashboard user.
     * @param userDTO {@linkplain UserDTO}
     * @return rows
     */
    int createOrUpdate(UserDTO userDTO);


    /**
     * find user by id.
     *
     * @param id primary key.
     * @return {@linkplain UserVO}
     */
    UserVO findById(Long id);


    /**
     * find user by openId.
     * @param miniOpenId wx open id
     * @return {@link UserVO}
     */
    UserVO findByOpenId(String miniOpenId);

    /**
     * find user by iphone.
     * @param iphone  user iphone
     * @return {@link UserVO}
     */
    UserVO findUserByIphone(String iphone);
}
