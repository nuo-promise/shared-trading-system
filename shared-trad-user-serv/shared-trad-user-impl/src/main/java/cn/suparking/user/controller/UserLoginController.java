package cn.suparking.user.controller;

import cn.suparking.common.api.utils.SpkCommonAssert;
import cn.suparking.user.api.beans.SessionKeyDTO;
import cn.suparking.user.api.vo.SessionKeyVO;
import cn.suparking.user.service.intf.UserLoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@Slf4j
@RefreshScope
@RestController
@RequestMapping("/userLogin")
public class UserLoginController {

    private final UserLoginService userLoginService;

    public UserLoginController(final UserLoginService userLoginService) {
        this.userLoginService = userLoginService;
    }

    /**
     * 根据code获取openId和sessionKey.
     *
     * @param sessionKeyDTO {@linkplain SessionKeyDTO}
     * @return {@linkplain SessionKeyVO}
     */
    @PostMapping("/getSessionKey")
    public SessionKeyVO getSessionKey(@Valid @RequestBody final SessionKeyDTO sessionKeyDTO) {
        return Optional.ofNullable(sessionKeyDTO).map(item -> {
            SpkCommonAssert.notBlank(sessionKeyDTO.getCode(), "code不能为空");
            return userLoginService.getSessionKey(sessionKeyDTO);
        }).orElseGet(() -> new SessionKeyVO());
    }
}
