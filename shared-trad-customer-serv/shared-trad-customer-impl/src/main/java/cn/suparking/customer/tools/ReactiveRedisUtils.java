package cn.suparking.customer.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

public class ReactiveRedisUtils {

    private static final Logger LOG = LoggerFactory.getLogger(ReactiveRedisUtils.class);

    private static final ReactiveRedisTemplate<String, Object> REDISTEMPLATE = BeansManager.getBean("ReactiveRedisTemplate", ReactiveRedisTemplate.class);

    /**
     * 删除redis key.
     * @param key string
     * @return {@link Mono}
     */
    public static Mono<Boolean> deleteValue(final String key) {
        return REDISTEMPLATE.opsForValue().delete(key);
    }

    /**
     * putData.
     * @param key the key
     * @param value the value
     * @return {@link Mono}
     */
    public static Mono<Boolean> putValue(final String key, final Object value) {
        return REDISTEMPLATE.opsForValue().set(key, value);
    }

    /**
     * put data and expiry time.
     * @param key the key
     * @param value the value
     * @param time time
     * @return {@link Mono}
     */
    public static Mono<Boolean> putValue(final String key, final Object value, final int time) {
        return REDISTEMPLATE.opsForValue().set(key, value, Duration.ofSeconds(time));
    }

    /**
     * get value by key.
     * @param key the key
     * @return {@link Mono}
     */
    public static Mono<Object> getData(final String key) {
        return REDISTEMPLATE.opsForValue().get(key);
    }

    /**
     * 查询是否存在某Key.
     * @param key pattern key.
     * @return {@link Flux}
     */
    public static Flux<String> getKeys(final String key) {
        return REDISTEMPLATE.keys(key);
    }

    /**
     * 判断Redis 是否存在此key.
     * @param key the key
     * @return {@link Mono}
     */
    public static Mono<Boolean> hasKey(final String key) {
        return REDISTEMPLATE.hasKey(key);
    }
}
