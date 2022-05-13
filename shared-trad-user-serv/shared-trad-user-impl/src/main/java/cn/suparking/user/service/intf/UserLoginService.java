package cn.suparking.user.service.intf;

import cn.suparking.user.api.beans.SessionKeyDTO;
import cn.suparking.user.api.vo.SessionKeyVO;

public interface UserLoginService {

    /**
     * 根据code获取openId和sessionKey.
     *
     * @param sessionKeyDTO {@linkplain SessionKeyDTO}
     * @return {@linkplain SessionKeyVO}
     */
    SessionKeyVO getSessionKey(SessionKeyDTO sessionKeyDTO);
}
