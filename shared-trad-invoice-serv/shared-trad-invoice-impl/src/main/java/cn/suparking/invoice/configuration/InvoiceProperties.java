package cn.suparking.invoice.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 诺诺发票配置信息.
 *
 * @author ZDD
 * @date 2022/7/25 18:17:43
 */
@Data
@Component("InvoiceProperties")
@ConfigurationProperties(prefix = "sparking.invoice")
public class InvoiceProperties {
    private String dataUrl;
}
