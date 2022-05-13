package cn.suparking.customer.controller.user.controller;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.common.api.utils.SpkCommonAssert;
import cn.suparking.customer.controller.user.service.UserOperateService;
import cn.suparking.user.api.beans.SessionKeyDTO;
import cn.suparking.user.api.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UserOperateController {

    @Autowired
    private UserOperateService userOperateService;

    /**
     * 通过code获取session_key.
     *
     * @param sessionKeyDTO {@linkplain SessionKeyDTO}
     * @return {@linkplain SpkCommonResult}
     */
    @PostMapping("login")
    public SpkCommonResult getSessionKey(@Valid @RequestBody final SessionKeyDTO sessionKeyDTO) {
        return Optional.ofNullable(sessionKeyDTO)
                .map(item -> {
                    SpkCommonAssert.notBlank(sessionKeyDTO.getCode(), "code不能为空");
                    UserVO userVO = userOperateService.getSessionKey(sessionKeyDTO);
                    return SpkCommonResult.success(userVO);
                }).orElseGet(() -> SpkCommonResult.error("code不能为空"));
    }
}
