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
@Component
@ConfigurationProperties(prefix = "sparking.invoice")
public class InvoiceProperties {
    //请求URL
    private String nnfpURL;

    //税号
    private String saletaxnum;

    //身份编号
    private String identity;

    //商品编码
    private String spbm;

    //商品名称
    private String goodsName;
}
