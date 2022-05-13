package cn.suparking.user.service;

import cn.suparking.common.api.exception.SpkCommonException;
import cn.suparking.common.api.utils.HttpRequestUtils;
import cn.suparking.user.api.beans.SessionKeyDTO;
import cn.suparking.user.api.vo.SessionKeyVO;
import cn.suparking.user.constant.UserConstant;
import cn.suparking.user.service.intf.UserLoginService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Service
public class UserLoginServiceImpl implements UserLoginService {

    @Value("${wx.appid}")
    private String appid;

    @Value("${wx.secret}")
    private String secret;

    /**
     * 根据code获取openId和sessionKey.
     *
     * @param sessionKeyDTO {@linkplain SessionKeyDTO}
     * @return {@linkplain SessionKeyVO}
     */
    @Override
    public SessionKeyVO getSessionKey(final SessionKeyDTO sessionKeyDTO) {
        Map<String, Object> paramsMap = new LinkedHashMap<>();
        paramsMap.put("appid", appid);
        paramsMap.put("secret", secret);
        paramsMap.put("js_code", sessionKeyDTO.getCode());
        paramsMap.put("grant_type", UserConstant.GRANT_TYPE);

        //请求微信小程序 code换session_key 接口
        JSONObject result = HttpRequestUtils.sendGet(UserConstant.JSCODE_TO_SESSION_URL, paramsMap);

        if (StringUtils.isNotBlank(result.getString("errcode"))) {
            throw new SpkCommonException("code换取session_key ======> 请求失败 [" + result.toJSONString() + "]");
        }

        SessionKeyVO sessionKeyVO = SessionKeyVO.builder()
                .openid(result.getString("openid"))
                .sessionKey(result.getString("session_key"))
                .unionid(result.getString("unionid"))
                .build();
        return sessionKeyVO;
    }
}
