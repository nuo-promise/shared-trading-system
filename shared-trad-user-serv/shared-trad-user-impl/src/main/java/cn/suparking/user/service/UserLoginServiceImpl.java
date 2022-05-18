package cn.suparking.user.service;

import cn.suparking.common.api.exception.SpkCommonException;
import cn.suparking.common.api.utils.HttpRequestUtils;
import cn.suparking.user.api.vo.PhoneInfoVO;
import cn.suparking.user.api.vo.RegisterVO;
import cn.suparking.user.api.vo.SessionVO;
import cn.suparking.user.configuration.properties.WxProperties;
import cn.suparking.user.constant.UserConstant;
import cn.suparking.user.service.intf.UserLoginService;
import cn.suparking.user.tools.ReactiveRedisUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class UserLoginServiceImpl implements UserLoginService {

    /**
     * wx login code.
     */
    private static final int WX_LOGIN_CODE_EXPIRED = 300;

    @Resource
    private WxProperties wxproperties;

    /**
     * 根据code获取openId和sessionKey.
     *
     * @param code wx login code.
     * @return {@linkplain RegisterVO}
     */
    @Override
    public SessionVO getSessionKey(final String code) {
        String sessionInfo = getValue(code);
        if (StringUtils.isNotBlank(sessionInfo)) {
            return JSON.parseObject(sessionInfo, SessionVO.class);
        } else {
            Map<String, Object> paramsMap = new LinkedHashMap<>();
            paramsMap.put("appid", wxproperties.getAppid());
            paramsMap.put("secret", wxproperties.getSecret());
            paramsMap.put("js_code", code);
            paramsMap.put("grant_type", UserConstant.GRANT_TYPE);

            //请求微信小程序 code换session_key 接口
            JSONObject result = HttpRequestUtils.sendGet(UserConstant.JSCODE_TO_SESSION_URL, paramsMap);

            return Optional.ofNullable(result).map(item -> {
                if (StringUtils.isNotBlank(item.getString("errcode"))) {
                    throw new SpkCommonException("code换取session_key ======> 请求失败 [" + item.toJSONString() + "]");
                }
                SessionVO session = SessionVO.builder()
                        .code(code)
                        .openid(item.getString("openid"))
                        .sessionKey(item.getString("session_key"))
                        .unionId(item.getString("unionid"))
                        .build();

                opsValue(session);
                return session;
            }).orElse(null);
        }
    }

    /**
     * get Access token.
     */
    @Override
    public void getAccessToken() {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("appid", wxproperties.getAppid());
        params.put("secret", wxproperties.getSecret());
        params.put("grant_type", UserConstant.ACCESS_TOKEN_GRANT_TYPE);

        JSONObject result = HttpRequestUtils.sendGet(UserConstant.ACCESS_TOKEN_URL, params);

        if (Objects.nonNull(result) && StringUtils.isNotBlank(result.getString("errcode"))) {
            throw new SpkCommonException(" getAccessToken ==========> 请求失败 [" + result.toJSONString() + "]");
        }
        opsAccessToken(result.getString("access_token"), result.getInteger("expires_in") * 1000);
    }

    @Override
    public PhoneInfoVO getPhoneInfo(final String phoneCode) {
        String accessToken = (String) ReactiveRedisUtils.getData(UserConstant.WX_ACCESS_TOKEN_KEY).block(Duration.ofMillis(3000));
        if (StringUtils.isNotBlank(accessToken)) {
            Map<String, Object> params = new LinkedHashMap<>();
            params.put("code", phoneCode);
            JSONObject result = HttpRequestUtils.sendPost(UserConstant.PHONE_NUMBER_URL + accessToken, params);

            return Optional.ofNullable(result).map(item -> {
                if (item.getInteger("errcode") != 0) {
                    throw new SpkCommonException("getPhoneInfo 错误 ======> 请求失败 [" + item.toJSONString() + "]");
                }
                JSONObject phoneInfo = item.getJSONObject("phone_info");
                JSONObject waterMark = phoneInfo.getJSONObject("watermark");
                PhoneInfoVO.WaterMarkVO waterMarkVO = PhoneInfoVO.WaterMarkVO.builder()
                        .appid(waterMark.getString("appid"))
                        .timestamp(waterMark.getLong("timestamp"))
                        .build();
                return PhoneInfoVO.builder()
                        .phoneNumber(phoneInfo.getString("phoneNumber"))
                        .purePhoneNumber(phoneInfo.getString("purePhoneNumber"))
                        .countryCode(phoneInfo.getString("countryCode"))
                        .waterMark(waterMarkVO)
                        .build();
            }).orElse(null);
        }
        return null;
    }

    /**
     * save data.
     * @param sessionVO {@link SessionVO}
     */
    private void opsValue(final SessionVO sessionVO) {
        ReactiveRedisUtils.putValue(sessionVO.getCode(), JSON.toJSONString(sessionVO), WX_LOGIN_CODE_EXPIRED).subscribe(
            flag -> {
                if (flag) {
                    log.info("Key= " + sessionVO.getCode() + " save redis success!");
                } else {
                    log.info("Key= " + sessionVO.getCode() + " save redis failed!");
                }
            }
        );
    }

    /**
     * save data.
     * @param accessToken accessToken
     * @param expiredTime time
     */
    private void opsAccessToken(final String accessToken, final int expiredTime) {
        ReactiveRedisUtils.putValue(UserConstant.WX_ACCESS_TOKEN_KEY, accessToken, expiredTime).subscribe(
            flag -> {
                if (flag) {
                    log.info("Key= " + UserConstant.WX_ACCESS_TOKEN_KEY + " save redis success!");
                } else {
                    log.info("Key= " + UserConstant.WX_ACCESS_TOKEN_KEY + " save redis failed!");
                }
            }
        );
    }

    /**
     * 获取 code 是否存在.
     * @param code wx login code
     * @return session string
     */
    private String getValue(final String code) {
        return (String) ReactiveRedisUtils.getData(code).block(Duration.ofMillis(3000));
    }
}
