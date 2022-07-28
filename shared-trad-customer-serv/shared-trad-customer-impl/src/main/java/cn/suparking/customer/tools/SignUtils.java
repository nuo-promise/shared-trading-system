package cn.suparking.customer.tools;

import cn.suparking.common.api.utils.DateUtils;
import cn.suparking.customer.configuration.properties.SharedProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Slf4j
@Component
public class SignUtils {

    @Resource
    private SharedProperties sharedProperties;

    //声明一个本静态工具类对象
    private static SignUtils signUtils;

    /**
     * 加入注解@postcontruct来初始化这个bean.
     */
    @PostConstruct
    public void init() {
        signUtils = this;
        signUtils.sharedProperties = this.sharedProperties;
    }

    /**
     * check sign.
     *
     * @param sign     sign.
     * @param deviceNo deviceNo
     * @return Boolean
     */
    public static Boolean invoke(final String sign, final String deviceNo) {
        return md5(signUtils.sharedProperties.getSecret() + deviceNo + DateUtils.currentDate() + signUtils.sharedProperties.getSecret(), sign);
    }

    /**
     * MD5.
     *
     * @param data  the data
     * @param token the token
     * @return boolean
     */
    private static boolean md5(final String data, final String token) {
        String keyStr = DigestUtils.md5Hex(data.toUpperCase()).toUpperCase();
        log.info("Mini MD5 Value: " + keyStr);
        if (keyStr.equals(token)) {
            return true;
        } else {
            log.warn("Mini Current MD5 :" + keyStr + ", Data Token : " + token);
        }
        return false;
    }
}
