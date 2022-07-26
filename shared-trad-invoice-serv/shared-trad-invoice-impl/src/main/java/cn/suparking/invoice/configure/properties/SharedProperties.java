package cn.suparking.invoice.configure.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component("SharedProperties")
@ConfigurationProperties(prefix = "sparking")
public class SharedProperties {

    private Integer corePoolSize;

    private Integer queryInterval;

    private Integer orderInterval;
}
