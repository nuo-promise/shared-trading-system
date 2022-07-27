package api.beans;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceModelDTO implements Serializable {

    private static final long serialVersionUID = 3914877750106917179L;

    /**
     * 发票记录ID.
     */
    private Long id;

    /**
     * 用户ID.
     */
    private Long userId;

    /**
     * 场库编号.
     */
    private String projectNo;

    /**
     * 购方企业名称.
     */
    private String buyerAme;

    /**
     * 购方企业税号.
     */
    private String taxNum;

    /**
     * 购方企业地址.
     */
    private String address;

    /**
     * 购方企业银行开户行及账号.
     */
    private String account;

    /**
     * 购方企业电话.
     */
    private String telePhone;

    /**
     * 订单号.
     */
    private String orderNo;

    /**
     * 单据时间.
     */
    private Date invoiceDate;

    /**
     * 销方企业税号.
     */
    private String saleTaxNum;

    /**
     * 销方企业银行开户行及账号.
     */
    private String saleAccount;

    /**
     * 销方企业电话.
     */
    private String salePhone;

    /**
     * 销方企业地址.
     */
    private String saleAddress;

    /**
     * 发票类型，1:正票;2：红票.
     */
    private String kpType;

    /**
     * 开票人.
     */
    private String clerk;

    /**
     * 收款人.
     */
    private String payee;

    /**
     * 复核人.
     */
    private String checker;

    /**
     * 推送方式 -1 不推送 0 邮箱 1 手机  2 邮箱&手机.
     */
    private String tsfs;

    /**
     * 推送邮箱.
     */
    private String email;

    /**
     * 推送手机号.
     */
    private String phone;

    /**
     * 发票请求流水号.
     */
    private String fpqqlsh;

    /**
     * 创建时间.
     */
    private Timestamp dateCreated;

    /**
     * 更新时间.
     */
    private Timestamp dateUpdated;

    private List<InvoiceDetail> invoiceDetailList;

    /**
     * 组织好的发票数据.
     */
    private String makeInvoiceData;
}
