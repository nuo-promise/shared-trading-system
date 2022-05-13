package cn.suparking.user.controller;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.common.api.utils.SpkCommonAssert;
import cn.suparking.common.api.utils.SpkCommonResultMessage;
import cn.suparking.user.api.beans.UserDTO;
import cn.suparking.user.vo.UserVO;
import cn.suparking.user.service.intf.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

/**
 * User Controller.
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("user")
public class UserController {

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    /**
     * create user.
     * @param userDTO {@linkplain UserDTO}
     * @return {@linkplain SpkCommonResult}
     */
    @PostMapping("")
    public SpkCommonResult createSharedUser(@Valid @RequestBody final UserDTO userDTO) {
        return Optional.ofNullable(userDTO)
                .map(item -> {
                    SpkCommonAssert.notBlank(item.getIphone(), SpkCommonResultMessage.PARAMETER_ERROR + ": iphone is not blank");
                    SpkCommonAssert.notNull(item.getRegisterType(), SpkCommonResultMessage.PARAMETER_ERROR + ": registerType is not null");
                    Integer createCount = userService.createOrUpdate(userDTO);
                    return SpkCommonResult.success(SpkCommonResultMessage.CREATE_SUCCESS, createCount);
                }).orElseGet(() -> SpkCommonResult.error(SpkCommonResultMessage.USER_CREATE_USER_ERROR));
    }

    /**
     * detail user.
     *
     * @param id dashboard user id.
     * @return {@linkplain SpkCommonResult}
     */
    @GetMapping("/{id}")
    public SpkCommonResult detailUser(@PathVariable("id") final Long id) {
        UserVO userVO = userService.findById(id);
        return Optional.ofNullable(userVO)
                .map(item -> SpkCommonResult.success(SpkCommonResultMessage.DETAIL_SUCCESS, item))
                .orElseGet(() -> SpkCommonResult.error(SpkCommonResultMessage.USER_QUERY_ERROR));
    }
}
