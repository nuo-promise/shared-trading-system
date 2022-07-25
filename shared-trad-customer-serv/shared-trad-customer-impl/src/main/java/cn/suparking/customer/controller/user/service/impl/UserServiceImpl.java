package cn.suparking.customer.controller.user.service.impl;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.common.api.utils.DateUtils;
import cn.suparking.common.api.utils.HttpUtils;
import cn.suparking.common.api.utils.SpkCommonResultMessage;
import cn.suparking.customer.configuration.properties.SharedProperties;
import cn.suparking.customer.configuration.properties.SmsProperties;
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
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static cn.suparking.common.api.utils.SpkCommonResultMessage.USER_PHONE_SMS_ERROR;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    /**
     * wx user info expired timeout.
     */
    private static final int WX_USER_EXPIRED_TIME = 86400;

    // 短信存储时间 60 s.
    private static final int PHONE_SMS_CODE_EXPIRED_TIME = 180;

    @Resource
    private SmsProperties smsProperties;

    @Resource
    private SharedProperties sharedProperties;

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
    @Transactional
    public UserVO register(final MiniRegisterDTO miniRegisterDTO) {
        //1.根据code获取openId和sessionKey
        SessionVO sessionVO = userTemplateService.getSessionKey(miniRegisterDTO.getCode());
        return Optional.ofNullable(sessionVO).map(item -> {
            /**
             * 解析手机号码,然后注册用户
             * 1. 线上接口获取accessToken
             * */
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

    @Override
    public SpkCommonResult getPhoneCode(final String sign, final String phone) {
        // 校验 sign
        if (!invoke(sign, phone)) {
            return SpkCommonResult.error(SpkCommonResultMessage.SIGN_NOT_VALID);
        }
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("phone", phone);
        log.info("发送短信验证码请求参数: " + requestMap);
        JSONObject resultObj = HttpUtils.sendPost(smsProperties.getUrl(), JSON.toJSONString(requestMap));
        log.info("发送短信验证码返回结果: " + resultObj);
        if (Objects.nonNull(resultObj) && resultObj.containsKey("code") && resultObj.getString("code").equals("00000")) {
            String smsCode = resultObj.getString("smsCode");
            if (StringUtils.isNotBlank(smsCode)) {
                opsSmsValue(phone, smsCode);
                return SpkCommonResult.success("发送成功");
            }
        }
        return SpkCommonResult.error("发送短信验证码失败");
    }

    @Override
    public SpkCommonResult changeUserPhone(final String sign, final UserDTO userDTO) {
        // 校验 sign
        if (!invoke(sign, userDTO.getPhoneCode())) {
            return SpkCommonResult.error(SpkCommonResultMessage.SIGN_NOT_VALID);
        }

        String smsCode = getSmsValue("MINI_SMS_" + userDTO.getIphone());
        if (StringUtils.isBlank(smsCode)) {
            return SpkCommonResult.error(USER_PHONE_SMS_ERROR + ", 短信验证码已过期");
        }

        if (!smsCode.equals(userDTO.getPhoneCode())) {
            return SpkCommonResult.error(USER_PHONE_SMS_ERROR + ", 短信验证码不正确");
        }

        UserVO userVO = userTemplateService.getUserVoByIphone(userDTO.getIphone());
        if (Objects.nonNull(userVO)) {
            return SpkCommonResult.error(USER_PHONE_SMS_ERROR + ", 手机号已被绑定");
        }

        userVO = userTemplateService.getUserByOpenId(userDTO.getMiniOpenId());
        if (Objects.isNull(userVO)) {
            return SpkCommonResult.error(USER_PHONE_SMS_ERROR + ", 用户不存在");
        }

        userDTO.setId(userVO.getId());
        userDTO.setUserName(userVO.getUserName());
        userDTO.setPassword(userVO.getPassword());
        userDTO.setNickName(userVO.getNickName());
        userDTO.setMiniOpenId(userVO.getMiniOpenId());
        userDTO.setOpenId(userVO.getOpenId());
        userDTO.setUnionId(userVO.getUnionId());
        userDTO.setEnabled(userVO.getEnabled());
        userDTO.setRegisterType(userVO.getRegisterType());
        userDTO.setMerchantId(userVO.getMerchantId());

        Integer count = userTemplateService.createSharedUser(userDTO);
        if (Objects.nonNull(count) && count.equals(1)) {
            return SpkCommonResult.success("修改成功");
        }
        return SpkCommonResult.error(USER_PHONE_SMS_ERROR + ", 修改失败");
    }

    /**
     * check sign.
     * @param sign sign.
     * @param deviceNo deviceNo
     * @return Boolean
     */
    private Boolean invoke(final String sign, final String deviceNo) {
        return md5(sharedProperties.getSecret() + deviceNo + DateUtils.currentDate() + sharedProperties.getSecret(), sign);
    }

    /**
     * MD5.
     * @param data the data
     * @param token the token
     * @return boolean
     */
    private boolean md5(final String data, final String token) {
        String keyStr = DigestUtils.md5Hex(data.toUpperCase()).toUpperCase();
        log.info("Mini MD5 Value: " + keyStr);
        if (keyStr.equals(token)) {
            return true;
        } else {
            log.warn("Mini Current MD5 :" + keyStr + ", Data Token : " + token);
        }
        return false;
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

    /**
     * 存储短信验证码.
     * @param phone 手机号码
     * @param smsCode 短信验证码
     */
    private void opsSmsValue(final String phone, final String smsCode) {
        ReactiveRedisUtils.putValue("MINI_SMS_" + phone, smsCode, PHONE_SMS_CODE_EXPIRED_TIME).subscribe(
            flag -> {
                if (flag) {
                    log.info("Key= " + phone + " save redis success!");
                } else {
                    log.info("Key= " + phone + " save redis failed!");
                }
            }
        );
    }

    /**
     * 根据Key 获取短信验证码.
     * @param key key
     * @return String
     */
    private String getSmsValue(final String key) {
        return (String) ReactiveRedisUtils.getData(key).block(Duration.ofMillis(3000));
    }
}
