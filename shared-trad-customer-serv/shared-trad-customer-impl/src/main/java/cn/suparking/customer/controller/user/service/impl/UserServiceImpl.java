package cn.suparking.customer.controller.user.service.impl;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.customer.controller.user.service.UserService;
import cn.suparking.customer.feign.user.UserTemplateService;
import cn.suparking.customer.tools.ReactiveRedisUtils;
import cn.suparking.user.api.beans.MiniRegisterDTO;
import cn.suparking.user.api.beans.UserDTO;
import cn.suparking.user.api.enums.RegisterType;
import cn.suparking.user.api.enums.UserStatus;
import cn.suparking.user.api.vo.PhoneInfoVO;
import cn.suparking.user.api.vo.SessionVO;
import cn.suparking.user.api.vo.UserVO;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    /**
     * wx user info expired timeout.
     */
    private static final int WX_USER_EXPIRED_TIME = 86400;

    private final UserTemplateService userTemplateService;

    public UserServiceImpl(final UserTemplateService userTemplateService) {
        this.userTemplateService = userTemplateService;
    }

    /**
     * 一键登录.
     *
     * @param miniRegisterDTO {@linkplain MiniRegisterDTO}
     * @return {@linkplain SpkCommonResult}
     */
    @Override
    public UserVO register(final MiniRegisterDTO miniRegisterDTO) {
        //1.根据code获取openId和sessionKey
        SessionVO sessionVO = userTemplateService.getSessionKey(miniRegisterDTO.getCode());
        return Optional.ofNullable(sessionVO).map(item -> {
            // 解析手机号码,然后注册用户
            PhoneInfoVO phoneInfoVO = userTemplateService.getPhoneInfo(miniRegisterDTO.getPhoneCode());
            return Optional.ofNullable(phoneInfoVO).map(phone -> {
                // 调用用户创建接口准备落库
                UserDTO userDTO = UserDTO.builder()
                        .iphone(phone.getPhoneNumber())
                        .miniOpenId(item.getOpenid())
                        .unionId(item.getUnionId())
                        .enabled(UserStatus.ACTIVE.getCode())
                        .registerType(RegisterType.WECHAT_MINI.getCode())
                        .build();
                Integer count = userTemplateService.createSharedUser(userDTO);
                if (Objects.nonNull(count) && count.equals(1)) {
                    UserVO userVO = userTemplateService.getUserByOpenId(userDTO.getMiniOpenId());
                    if (Objects.nonNull(userVO)) {
                        opsValue(userVO);
                    }
                    return userVO;
                }
                return null;
            }).orElse(null);
        }).orElse(null);
    }

    /**
     * mini user login.
     * @param code wx code
     * @return {@link UserVO}
     */
    @Override
    public UserVO login(final String code) {
        UserVO userVO = null;
        // 现根据 code 拿到用户信息
        SessionVO sessionVO = userTemplateService.getSessionKey(code);
        if (Objects.nonNull(sessionVO) && StringUtils.isNotBlank(sessionVO.getOpenid())) {
           // 拿着 openId 去查询用户信息返回给前端
            userVO = userTemplateService.getUserByOpenId(sessionVO.getOpenid());
        }
        return userVO;
    }

    /**
     * save data.
     * @param userVO {@link UserVO}
     */
    private void opsValue(final UserVO userVO) {
        ReactiveRedisUtils.putValue(userVO.getMiniOpenId(), JSON.toJSONString(userVO), WX_USER_EXPIRED_TIME).subscribe(
            flag -> {
                if (flag) {
                    log.info("Key= " + userVO.getMiniOpenId() + " save redis success!");
                } else {
                    log.info("Key= " + userVO.getMiniOpenId() + " save redis failed!");
                }
            }
        );
    }
}
