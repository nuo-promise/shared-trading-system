package cn.suparking.customer.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "spring.parkmq")
public class ParkmqProperties {
    private String host;

    private int port;

    private String userName;

    private String passWord;

    private String exchange;

    private int consumerPrefetch;

    private int concurrentConsumer;
}
