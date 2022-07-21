package cn.suparking.order.feign.order.fallback;

import cn.suparking.order.feign.order.UserTemplateService;
import com.alibaba.fastjson.JSONObject;
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
     *
     * @param cause {@link Throwable}
     * @return {@link UserTemplateService}
     */
    @Override
    public UserTemplateService create(final Throwable cause) {
        Arrays.stream(cause.getStackTrace()).forEach(item -> log.error(item.toString()));
        return new UserTemplateService() {
            @Override
            public JSONObject getUserByIphone(final String iphone) {
                log.error("UserTemplateService: getSessionKey error: " + cause.getMessage());
                return null;
            }

            @Override
            public JSONObject detailUser(final Long id) {
                log.error("UserTemplateService: getUserByOpenId error: " + cause.getMessage());
                return null;
            }
        };
    }
}
