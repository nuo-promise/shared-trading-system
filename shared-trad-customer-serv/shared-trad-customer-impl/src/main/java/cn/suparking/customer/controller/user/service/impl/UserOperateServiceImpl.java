package cn.suparking.customer.controller.user.service.impl;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.customer.controller.user.service.UserOperateService;
import cn.suparking.customer.feign.user.UserTemplateService;
import cn.suparking.user.api.beans.SessionKeyDTO;
import cn.suparking.user.api.vo.SessionKeyVO;
import cn.suparking.user.api.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserOperateServiceImpl implements UserOperateService {

    private final UserTemplateService userTemplateService;

    public UserOperateServiceImpl(final UserTemplateService userTemplateService) {
        this.userTemplateService = userTemplateService;
    }

    /**
     * 一键登录.
     *
     * @param sessionKeyDTO {@linkplain SessionKeyDTO}
     * @return {@linkplain SpkCommonResult}
     */
    @Override
    public UserVO getSessionKey(final SessionKeyDTO sessionKeyDTO) {
        //1.根据code获取openId和sessionKey
        SessionKeyVO sessionKey = userTemplateService.getSessionKey(sessionKeyDTO);

        return new UserVO();
    }
}
