package cn.suparking.user.service.intf;

import cn.suparking.user.api.beans.UserDTO;
import cn.suparking.user.dao.vo.UserVO;

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
}