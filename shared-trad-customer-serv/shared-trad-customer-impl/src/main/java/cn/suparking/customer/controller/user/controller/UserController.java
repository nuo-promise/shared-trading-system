package cn.suparking.customer.controller.user.controller;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.common.api.utils.SpkCommonAssert;
import cn.suparking.common.api.utils.SpkCommonResultMessage;
import cn.suparking.customer.controller.user.service.UserService;
import cn.suparking.user.api.beans.MiniLoginDTO;
import cn.suparking.user.api.beans.MiniRegisterDTO;
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
@RequestMapping("user-api")
public class UserController {

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    /**
     * 通过code获取session_key.
     *
     * @param miniRegisterDTO {@linkplain MiniRegisterDTO}
     * @return {@linkplain SpkCommonResult}
     */
    @PostMapping("register")
    public SpkCommonResult getSessionKey(@Valid @RequestBody final MiniRegisterDTO miniRegisterDTO) {
        return Optional.ofNullable(miniRegisterDTO)
                .map(item -> {
                    SpkCommonAssert.notBlank(miniRegisterDTO.getCode(), SpkCommonResultMessage.PARAMETER_ERROR + "code 不能为空");
                    return SpkCommonResult.success(userService.register(miniRegisterDTO));
                }).orElseGet(() -> SpkCommonResult.error("code不能为空"));
    }

    /**
     * 通过微信login 登录.
     * @param miniLoginDTO {@link MiniLoginDTO}
     * @return {@link SpkCommonResult}
     */
    @PostMapping("login")
    public SpkCommonResult login(@Valid @RequestBody final MiniLoginDTO miniLoginDTO) {
        return Optional.ofNullable(miniLoginDTO)
               .map(item -> {
                   SpkCommonAssert.notBlank(miniLoginDTO.getCode(), SpkCommonResultMessage.PARAMETER_ERROR + "code不能为空");
                   return SpkCommonResult.success(userService.login(miniLoginDTO.getCode()));
               }).orElseGet(() -> SpkCommonResult.error(SpkCommonResultMessage.PARAMETER_ERROR));

    }

}
