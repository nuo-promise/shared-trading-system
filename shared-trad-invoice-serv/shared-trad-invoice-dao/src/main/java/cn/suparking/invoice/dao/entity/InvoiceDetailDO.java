package cn.suparking.invoice.dao.entity;

import api.beans.InvoiceDetail;
import cn.suparking.common.api.configuration.SnowflakeConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceDetailDO implements Serializable {

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

    /**
     * build InvoiceSourceDO.
     *
     * @param invoiceDetail {@link InvoiceDetail}
     * @return {@link InvoiceDetailDO}
     */
    public static InvoiceDetailDO buildCarGroupOrderDO(final InvoiceDetail invoiceDetail) {
        return Optional.ofNullable(invoiceDetail).map(item -> {
            InvoiceDetailDO invoiceDetailDO = InvoiceDetailDO.builder()
                    .invoiceModelId(item.getInvoiceModelId())
                    .goodsname(item.getGoodsname())
                    .hsbz(item.getHsbz())
                    .taxrate(item.getTaxrate())
                    .spbm(item.getSpbm())
                    .fphxz(item.getFphxz())
                    .taxfreeamt(item.getTaxfreeamt())
                    .tax(item.getTax())
                    .taxamt(item.getTaxamt())
                    .build();
            if (Objects.isNull(item.getId())) {
                invoiceDetailDO.setId(SnowflakeConfig.snowflakeId());
            } else {
                invoiceDetailDO.setId(item.getId());
            }
            return invoiceDetailDO;
        }).orElse(null);
    }
}
