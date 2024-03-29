package cn.suparking.customer.controller.user.controller;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.common.api.utils.SpkCommonAssert;
import cn.suparking.common.api.utils.SpkCommonResultMessage;
import cn.suparking.customer.controller.user.service.UserService;
import cn.suparking.user.api.beans.MiniLoginDTO;
import cn.suparking.user.api.beans.MiniRegisterDTO;
import cn.suparking.user.api.beans.UserDTO;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    @GlobalTransactional(name = "shared-trad-customer-serv", rollbackFor = Exception.class)
    public SpkCommonResult getSessionKey(@Valid @RequestBody final MiniRegisterDTO miniRegisterDTO) {
        return Optional.ofNullable(miniRegisterDTO)
                .map(item -> {
                    SpkCommonAssert.notBlank(item.getCode(), SpkCommonResultMessage.PARAMETER_ERROR + "code 不能为空");
                    return SpkCommonResult.success(userService.register(item));
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
                   SpkCommonAssert.notBlank(item.getCode(), SpkCommonResultMessage.PARAMETER_ERROR + "code不能为空");
                   return SpkCommonResult.success(userService.login(item.getCode()));
               }).orElseGet(() -> SpkCommonResult.error(SpkCommonResultMessage.PARAMETER_ERROR));

    }

    /**
     * 获取短信验证码.
     * @param sign {@link String}
     * @param phone {@link String}
     * @return {@link SpkCommonResult}
     */
    @GetMapping("getSmsCode")
    public SpkCommonResult getPhoneCode(@RequestHeader("sign") final String sign, @RequestParam("phone") final String phone) {
        SpkCommonAssert.notBlank(phone, SpkCommonResultMessage.PARAMETER_ERROR + "phone不能为空");
        return userService.getPhoneCode(sign, phone);
    }

    /**
     * 根据用户 openId 更新手机号.
     * @param sign sign
     * @param userDTO {@link UserDTO}
     * @return {@link SpkCommonResult}
     */
    @PostMapping("updatePhone")
    public SpkCommonResult changeUserPhone(@RequestHeader("sign") final String sign, @RequestBody final UserDTO userDTO) {
        return userService.changeUserPhone(sign, userDTO);
    }
}
