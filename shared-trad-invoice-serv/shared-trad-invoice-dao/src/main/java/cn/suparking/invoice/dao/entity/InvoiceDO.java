package cn.suparking.invoice.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 发票抬头信息.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceDO implements Serializable {
    /**
     * 发票记录ID.
     */
    private String id;

    /**
     * 发票二维码路径.
     */
    private String qrcode;

    /**
     * 发票显示URL路径.
     */
    private String invoicePath;

    /**
     * 发送邮箱地址.
     */
    private String email;

    /**
     * 发送发票的手机号码.
     */
    private String iphone;

    /**
     * 开发票人id.
     */
    private String userId;

    /**
     * 开票金额.
     */
    private Long amount;

    /**
     * 发票类型: 1:普通发票 2:增票.
     */
    private Integer invoiceType;

    /**
     * 获取类型: 1:online 2:email 3:pick.
     */
    private Integer accessType;

    /**
     * 自提地址.
     */
    private String pickAddress;

    /**
     * 快递地址.
     */
    private String courierAddress;

    /**
     * 发票内容开始时间.
     */
    private Timestamp dateInvoiceBegin;

    /**
     * 发票内容结束时间.
     */
    private Timestamp dateInvoiceEnd;

    /**
     * 创建时间.
     */
    private Timestamp dateCreated;

    /**
     * 更新时间.
     */
    private Timestamp dateUpdated;
}
