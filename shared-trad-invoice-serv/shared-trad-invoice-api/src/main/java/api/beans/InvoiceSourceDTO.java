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
public class InvoiceSourceDTO implements Serializable {

    private static final long serialVersionUID = 5607215789874263107L;

    /**
     * 主键id.
     */
    private Long id;

    /**
     * 用户id.
     */
    private Long userId;

    /**
     * 支付金额.
     */
    private Integer payAmount;

    /**
     * 订单号.
     */
    private String orderNo;

    /**
     * 开票状态 true: 已开票， false: 未开票.
     */
    private Boolean state;

    /**
     * 项目编号.
     */
    private String projectNo;

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

    /**
     * 开始时间 - 查询用.
     */
    private Long beginDate;

    /**
     * 结束时间 - 查询用.
     */
    private Long endDate;
}
