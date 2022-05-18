package cn.suparking.customer.feign.user.fallback;

import cn.suparking.customer.feign.user.UserTemplateService;
import cn.suparking.user.api.beans.UserDTO;
import cn.suparking.user.api.vo.PhoneInfoVO;
import cn.suparking.user.api.vo.SessionVO;
import cn.suparking.user.api.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * UserTemplateService hystrix 降级工厂.
 */
@Slf4j
@Component
public class UserTemplateFallbackFactory implements FallbackFactory<UserTemplateService> {

    /**
     * 降级将Throwable 作为入参传递.
     * @param cause {@link Throwable}
     * @return {@link UserTemplateService}
     */
    @Override
    public UserTemplateService create(final Throwable cause) {
        Arrays.stream(cause.getStackTrace()).forEach(item -> log.error(item.toString()));
        return new UserTemplateService() {
            @Override
            public SessionVO getSessionKey(final String code) {
                log.error("UserTemplateService: getSessionKey error: " + cause.getMessage());
                return null;
            }

            @Override
            public UserVO getUserByOpenId(final String miniOpenId) {
                log.error("UserTemplateService: getUserByOpenId error: " + cause.getMessage());
                return null;
            }

            @Override
            public Integer createSharedUser(final UserDTO userDTO) {
                log.error("UserTemplateService: createSharedUser error: " + cause.getMessage());
                return null;
            }

            @Override
            public PhoneInfoVO getPhoneInfo(final String phoneCode) {
                log.error("UserTemplateService: getPhoneInfo error: " + cause.getMessage());
                return null;
            }
        };
    }
}
