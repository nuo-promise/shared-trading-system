package cn.suparking.invoice.dao.vo;

import api.beans.InvoiceSourceDTO;
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
public class InvoiceSourceVO implements Serializable {

    private static final long serialVersionUID = -5933208002676594860L;

    /**
     * 主键id.
     */
    private String id;

    /**
     * 用户id.
     */
    private String userId;

    /**
     * 支付金额.
     */
    private Integer payAmount;

    /**
     * 订单号.
     */
    private String orderNo;

    /**
     * 开票状态 2:开票完成 20:开票中 21:开票成功签章中 22:开票失败 24:开票成功签章失败 .
     */
    private String state;

    /**
     * 项目编号.
     */
    private String projectNo;

    /**
     * 项目名称.
     */
    private String projectName;

    /**
     * 开始时间.
     */
    private String startTime;

    /**
     * 结束时间.
     */
    private String endTime;

    /**
     * 支付方式.
     */
    private String payType;

    /**
     * 支付通道.
     */
    private String payChannel;

    /**
     * 支付时间.
     */
    private Long payTime;

    /**
     * 源数据文档.
     */
    private String sourceDoc;

    /**
     * 源数据主键.
     */
    private String sourceId;

    /**
     * 源数据合约协议id.
     */
    private String protocolId;

    /**
     * 发票信息编号.
     */
    private String invoiceCode;

    /**
     * 终端编号.
     */
    private String termNo;

    /**
     * 操作人.
     */
    private String operator;

    private String address;
}
