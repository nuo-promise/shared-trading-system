package cn.suparking.data.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "sparking.pool")
public class ThreadPoolProperties {

    private Integer coreSize;

    private Integer maxSize;

    private Integer keepAlive;

    private Integer queueCapacity;
}
