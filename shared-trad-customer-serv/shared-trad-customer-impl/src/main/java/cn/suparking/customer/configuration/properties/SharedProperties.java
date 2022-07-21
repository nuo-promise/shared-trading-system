package cn.suparking.customer.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component("SharedProperties")
@ConfigurationProperties(prefix = "sparking")
public class SharedProperties {
    private String secret;

    private String discountUrl;

    private Integer corePoolSize;

    private Integer queryInterval;

    private Integer orderInterval;
}
