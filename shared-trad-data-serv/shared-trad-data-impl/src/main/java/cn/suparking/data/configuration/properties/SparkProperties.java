package cn.suparking.data.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component("SparkProperties")
@ConfigurationProperties(prefix = "sparking.bs")
public class SparkProperties {

    private String url;
}
