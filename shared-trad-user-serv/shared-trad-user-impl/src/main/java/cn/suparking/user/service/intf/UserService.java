package cn.suparking.user.service.intf;

import cn.suparking.user.api.beans.UserDTO;

public interface UserService {

    /**
     * create or update dashboard user.
     * @param userDTO {@linkplain UserDTO}
     * @return rows
     */
    int createOrUpdate(UserDTO userDTO);
}
