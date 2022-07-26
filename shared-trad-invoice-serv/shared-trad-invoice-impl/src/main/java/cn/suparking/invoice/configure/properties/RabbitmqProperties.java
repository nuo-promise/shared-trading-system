package cn.suparking.invoice.configure.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component("RabbitmqProperties")
@ConfigurationProperties(prefix = "spring.rabbitmq")
public class RabbitmqProperties {

    private String host;

    private int port;

    private String userName;

    private String passWord;

    private String exchange;

    private Boolean enable;

    private int consumerPrefetch;

    private int concurrentConsumer;

    private Integer queueLength;

    private Long threadTimeout;

}