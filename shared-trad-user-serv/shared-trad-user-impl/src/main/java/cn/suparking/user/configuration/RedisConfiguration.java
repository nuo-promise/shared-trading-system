package cn.suparking.user.configuration;

import cn.suparking.user.configuration.properties.RedisLettucePoolProperties;
import cn.suparking.user.configuration.properties.RedisProperties;
import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;
import java.time.Duration;

@Configuration
public class RedisConfiguration {

    @Resource
    private RedisProperties redisProperties;

    @Resource
    private RedisLettucePoolProperties redisLettucePoolProperties;

    /**
     * Key/Value is String Serializer.
     * @return {@link RedisSerializer}
     */
    @Bean(name = "StringRedisSerializer")
    public RedisSerializer<String> redisKeySerializer() {
        return new StringRedisSerializer();
    }

    /**
     * Key/Value is Object Serializer.
     * @return {@link GenericFastJsonRedisSerializer}
     */
    @Bean(name = "GenericFastJsonRedisSerializer")
    public RedisSerializer<Object> redisValueSerializer() {
        return new GenericFastJsonRedisSerializer();
    }

    /**
     * Lettuce Pool Config.
     * @return {@link GenericObjectPoolConfig}
     */
    @Bean(name = "GenericObjectPoolConfig")
    public GenericObjectPoolConfig<Object> genericObjectPoolConfig() {
        GenericObjectPoolConfig<Object> config = new GenericObjectPoolConfig<>();
        config.setMaxIdle(redisLettucePoolProperties.getMaxIdle());
        config.setMinIdle(redisLettucePoolProperties.getMinIdle());
        config.setMaxTotal(redisLettucePoolProperties.getMaxActive());
        config.setMaxWaitMillis(redisLettucePoolProperties.getMaxWait());
        return config;
    }

    /**
     * Redis Lettuce Connection Factory.
     * 参考配置: https://blog.csdn.net/wangh92/article/details/81736820
     * @param config {@link GenericObjectPoolConfig}
     * @return {@link LettuceConnectionFactory}
     */
    @Primary
    @Bean(name = "ReactiveRedisConnectionFactory")
    public ReactiveRedisConnectionFactory lettuceConnectionFactory(@Qualifier(value = "GenericObjectPoolConfig") final GenericObjectPoolConfig<Object> config) {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setDatabase(redisProperties.getDatabase());
        redisStandaloneConfiguration.setHostName(redisProperties.getHost());
        redisStandaloneConfiguration.setPort(redisProperties.getPort());
        redisStandaloneConfiguration.setPassword(redisProperties.getPassword());

        LettuceClientConfiguration clientConfiguration = LettucePoolingClientConfiguration.builder()
                .commandTimeout(Duration.ofMillis(redisProperties.getTimeout()))
                .poolConfig(config)
                .build();

        return new LettuceConnectionFactory(redisStandaloneConfiguration, clientConfiguration);
    }

    /**
     * Redis Serialization.
     * @param redisKeySerializer {@link RedisSerializer}
     * @param redisValueSerializer {@link RedisSerializer}
     * @return {@link RedisSerializationContext}
     */
    @Bean(name = "RedisSerializationContext")
    public RedisSerializationContext<String, Object> redisSerializationContext(@Qualifier(value = "StringRedisSerializer") final RedisSerializer<String> redisKeySerializer,
                                                                               @Qualifier(value = "GenericFastJsonRedisSerializer") final RedisSerializer<Object> redisValueSerializer) {
        RedisSerializationContext.RedisSerializationContextBuilder<String, Object> builder = RedisSerializationContext.newSerializationContext();
        builder.key(redisKeySerializer);
        builder.value(redisValueSerializer);
        builder.hashKey(redisKeySerializer);
        builder.hashValue(redisValueSerializer);

        return builder.build();
    }

    /**
     * ReactiveRedisTemplate.
     * @param connectionFactory connection factory
     * @param redisSerializationContext redisSerializationContext
     * @return {@link RedisTemplate}
     */
    @Bean(name = "ReactiveRedisTemplate")
    public ReactiveRedisTemplate<String, Object> redisTemplate(@Qualifier(value = "ReactiveRedisConnectionFactory") final ReactiveRedisConnectionFactory connectionFactory,
                                                               @Qualifier(value = "RedisSerializationContext") final RedisSerializationContext<String, Object> redisSerializationContext) {

        return new ReactiveRedisTemplate<>(connectionFactory, redisSerializationContext);
    }
}
