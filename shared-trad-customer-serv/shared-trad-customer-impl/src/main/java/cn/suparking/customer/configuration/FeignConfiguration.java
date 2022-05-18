package cn.suparking.customer.configuration;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfiguration {

    /**
     * 声明 Feign 组件的日志级别.
     * @return {@link Logger.Level}
     */
    @Bean
    Logger.Level feignLogger() {
        return Logger.Level.FULL;
    }
}
