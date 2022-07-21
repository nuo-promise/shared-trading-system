package cn.suparking.user.controller;

import cn.suparking.common.api.utils.SpkCommonAssert;
import cn.suparking.common.api.utils.SpkCommonResultMessage;
import cn.suparking.user.api.vo.PhoneInfoVO;
import cn.suparking.user.api.vo.RegisterVO;
import cn.suparking.user.api.vo.SessionVO;
import cn.suparking.user.service.intf.UserLoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
     * @param code user wx login code
     * @return {@linkplain RegisterVO}
     */
    @PostMapping("/getSessionKey")
    public SessionVO getSessionKey(@RequestBody final String code) {
        return Optional.ofNullable(code).map(item -> {
            SpkCommonAssert.notBlank(code, SpkCommonResultMessage.PARAMETER_ERROR + "code 不能为空");
            return userLoginService.getSessionKey(code);
        }).orElse(null);
    }

    /**
     * get User Phone By phoneCode.
     * @param phoneCode phone code
     * @return {@link PhoneInfoVO}
     */
    @GetMapping("/getPhoneInfo")
    public PhoneInfoVO getPhoneInfo(@RequestParam("phoneCode") final String phoneCode) {
        return Optional.ofNullable(phoneCode).map(item -> {
            SpkCommonAssert.notBlank(phoneCode, SpkCommonResultMessage.PARAMETER_ERROR + "phoneCode 不能为空");
            return userLoginService.getPhoneInfo(phoneCode);
        }).orElse(null);
    }
}
