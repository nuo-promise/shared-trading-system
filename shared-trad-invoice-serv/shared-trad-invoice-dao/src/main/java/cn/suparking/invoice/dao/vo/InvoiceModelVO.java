package cn.suparking.invoice.dao.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceModelVO implements Serializable {

    private static final long serialVersionUID = 3914877750106917179L;

    /**
     * 发票记录ID.
     */
    private String id;

    /**
     * 用户ID.
     */
    private String userId;

    /**
     * 场库编号.
     */
    private String projectNo;

    /**
     * 购方企业名称.
     */
    private String buyername;

    /**
     * 购方企业税号.
     */
    private String taxnum;

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
    private String telephone;

    /**
     * 订单号.
     */
    private String orderno;

    /**
     * 单据时间.
     */
    private String invoicedate;

    /**
     * 销方企业税号.
     */
    private String saletaxnum;

    /**
     * 销方企业银行开户行及账号.
     */
    private String saleaccount;

    /**
     * 销方企业电话.
     */
    private String salephone;

    /**
     * 销方企业地址.
     */
    private String saleaddress;

    /**
     * 发票类型，1:正票;2：红票.
     */
    private String kptype;

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
     * 图片 url.
     */
    private String imgUrl;

    /**
     * PDF url.
     */
    private String pdfUrl;

    /**
     * 本地图片路径.
     */
    private String imgPath;

    /**
     * 本地PDF路径.
     */
    private String pdfPath;

    /**
     * 2:开票完成 20:开票中 21:开票成功签章中 22:开票失败 24:开票成功签章失败.
     */
    private String state;

    /**
     * 创建时间.
     */
    private Timestamp dateCreated;

    /**
     * 更新时间.
     */
    private Timestamp dateUpdated;

    /**
     * 关联订单数.
     */
    private Integer count;

    /**
     * 支付金额.
     */
    private Integer dueAmount;
}
