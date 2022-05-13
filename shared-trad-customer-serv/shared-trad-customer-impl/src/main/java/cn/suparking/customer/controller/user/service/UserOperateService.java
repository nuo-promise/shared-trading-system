package cn.suparking.customer.controller.user.service;


import cn.suparking.user.api.beans.SessionKeyDTO;
import cn.suparking.user.api.vo.UserVO;

public interface UserOperateService {
    UserVO getSessionKey(SessionKeyDTO sessionKeyDTO);
}
