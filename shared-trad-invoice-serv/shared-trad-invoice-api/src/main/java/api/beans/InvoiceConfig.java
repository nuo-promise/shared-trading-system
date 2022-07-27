package api.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceConfig {

    /**
     * 项目编号.
     */
    private String projectNo;

    /**
     * 税率.
     */
    private String taxRate;

    /**
     * 商品名称.
     */
    private String goodsName;

    /**
     * 商品编码.
     */
    private String spbm;

    /**
     * 开票金额限制.
     */
    private Integer invoiceLimit;

    /**
     * 开票员.
     */
    private String clerk;

    /**
     * 销方地址.
     */
    private String saleAddress;

    /**
     * 销方电话.
     */
    private String salePhone;

    /**
     * 销方银行账号： 开户行+账号.
     */
    private String saleAccount;

    /**
     * 诺诺网企业身份代码： 6位。.
     */
    private String jskpCode;

    /**
     * 部门门店ID（非必传, 可为null）.
     */
    private String deptid;

    /**
     * 允许开票订单类型.
     */
    private List<String> allowOrderTypeList;

    /**
     * 允许开票支付方式.
     */
    private List<String> allowPayTypeList;

    /**
     * 允许开票合约协议.
     */
    private List<String> allowProtocolIdList;

    /**
     * 允许开票支付方式其他中的具体支付渠道.
     */
    private List<String> allowPayChannelList;

    /**
     * 备注.
     */
    private String remark;
}
