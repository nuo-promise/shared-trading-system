package api.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceDetail implements Serializable {

    private static final long serialVersionUID = -7168607945096423482L;

    /**
     * 发票明细ID.
     */
    private Long id;

    /**
     * 发票记录表id.
     */
    private Long invoiceModelId;

    /**
     * 商品名称.
     */
    private String goodsname;

    /**
     * 单价含税标志 0 不含税 1 含税.
     */
    private String hsbz;

    /**
     * 税率.
     */
    private String taxrate;

    /**
     * 税收分类编码.
     */
    private String spbm;

    /**
     * 发票行性质，0:正常行;1:折扣行;2:被折扣行.
     */
    private String fphxz;

    /**
     * 不含税金额.
     */
    private String taxfreeamt;

    /**
     * 税额.
     */
    private String tax;

    /**
     * 含税金额.
     */
    private String taxamt;
}
