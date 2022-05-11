package cn.suparking.invoice.dao.vo;

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
public class InvoiceInfoVO implements Serializable {
    /**
     * 发票抬头唯一ID.
     */
    private String id;

    /**
     * 用户ID.
     */
    private String userId;

    /**
     * 抬头名称.
     */
    private String headName;

    /**
     * 发票税号.
     */
    private String taxCode;

    /**
     * 单位地址.
     */
    private String address;

    /**
     * 单位电话.
     */
    private String iphone;

    /**
     * 银行账号.
     */
    private String bankCode;

    /**
     * 联系人.
     */
    private String userName;

    /**
     * 联系人手机号.
     */
    private String userPhone;

    /**
     * 车牌号码.
     */
    private String plateNo;

    /**
     * 是否是默认 0:否 1:是.
     */
    private Integer defaultInfo;

    /**
     * 创建时间.
     */
    private Timestamp dateCreated;

    /**
     * 更新时间.
     */
    private Timestamp dateUpdated;
}
