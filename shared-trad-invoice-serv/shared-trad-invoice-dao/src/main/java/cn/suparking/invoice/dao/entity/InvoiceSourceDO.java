package cn.suparking.invoice.dao.entity;

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
public class InvoiceSourceDO implements Serializable {

    private static final long serialVersionUID = -5933208002676594860L;

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

    /**
     * build InvoiceSourceDO.
     *
     * @param invoiceSourceDTO {@link InvoiceSourceDTO}
     * @return {@link InvoiceSourceDTO}
     */
    public static InvoiceSourceDO buildCarGroupOrderDO(final InvoiceSourceDTO invoiceSourceDTO) {
        return Optional.ofNullable(invoiceSourceDTO).map(item -> {
            InvoiceSourceDO invoiceSourceDO = InvoiceSourceDO.builder()
                    .userId(item.getUserId())
                    .payAmount(item.getPayAmount())
                    .orderNo(item.getOrderNo())
                    .state(item.getState())
                    .projectNo(item.getProjectNo())
                    .startTime(item.getStartTime())
                    .endTime(item.getEndTime())
                    .payType(item.getPayType())
                    .payChannel(item.getPayChannel())
                    .payTime(item.getPayTime())
                    .sourceDoc(item.getSourceDoc())
                    .sourceId(item.getSourceId())
                    .protocolId(item.getProtocolId())
                    .invoiceCode(item.getInvoiceCode())
                    .termNo(item.getTermNo())
                    .operator(item.getOperator())
                    .build();
            if (Objects.isNull(item.getId())) {
                invoiceSourceDO.setId(SnowflakeConfig.snowflakeId());
            } else {
                invoiceSourceDO.setId(item.getId());
            }
            return invoiceSourceDO;
        }).orElse(null);
    }
}
