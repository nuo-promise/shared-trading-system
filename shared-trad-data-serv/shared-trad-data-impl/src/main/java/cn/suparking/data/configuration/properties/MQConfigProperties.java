package cn.suparking.data.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "sparking.message")
public class MQConfigProperties {

    private Integer queueLength;

    private Long threadTimeout;
}
