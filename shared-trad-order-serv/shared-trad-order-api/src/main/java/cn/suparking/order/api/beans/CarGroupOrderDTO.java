package cn.suparking.order.api.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

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

    private Long beginDate;

    private Long endDate;

    private Integer totalAmount;

    private Integer discountedAmount;

    private String payChannel;

    private String payType;

    private String userName;

    private String userMobile;

    private String orderState;

    private String invoiceState;

    private String refundState;

    private String termNo;

    private String projectNo;

    private String orderType;

    private String creator;

    private String modifier;

    /**
     * 创建时间.
     */
    private Timestamp dateCreated;

    /**
     * 更新时间.
     */
    private Timestamp dateUpdated;
}
