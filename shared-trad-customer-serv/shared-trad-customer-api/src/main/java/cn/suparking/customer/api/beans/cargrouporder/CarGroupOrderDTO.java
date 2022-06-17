package cn.suparking.customer.api.beans.cargrouporder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarGroupOrderDTO implements Serializable {

    private static final long serialVersionUID = -1031380009838779778L;

    private String id;

    private String userId;

    @NotNull
    @NotBlank
    private String orderNo;

    private String carGroupId;

    private String carTypeId;

    private String carTypeName;

    private String protocolId;

    private String protocolName;

    private Integer totalAmount;

    private Integer discountedAmount;

    private String payChannel;

    private String payType;

    private String userMobile;

    private String orderState;

    private String invoiceState;

    private String refundState;

    private String termNo;

    private String projectNo;

    private String orderType;

    /**
     * 开始时间.
     */
    private Long beginTime;

    /**
     * 结束时间.
     */
    private Long endTime;

    /**
     * 车辆类型id.
     */
    private String carTypeIds;

    /**
     * 应付金额（RMB分）.
     */
    private Integer dueAmount;

    /**
     * 如果为true则为未生效预约订单 / false则为已生效预约订单(包括正常已生效及已退费的).
     */
    private Boolean scheduled;

    /**
     * 备注.
     */
    private String remark;

    /**
     * 开票时间.
     */
    private Long invoiceTime;

    /**
     * 开票服务数据库对应的订单编号(可能含有@).
     */
    private String invoiceOrderNo;

    private String creator;

    /**
     * 创建时间.
     */
    private Long createTime;

    private String modifier;

    /**
     * 修改时间.
     */
    private Long modifyTime;
}
