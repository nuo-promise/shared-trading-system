package cn.suparking.order.api.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarGroupRefundOrderDTO implements Serializable {

    private static final long serialVersionUID = -2328381724600453603L;

    private String id;

    private String userId;

    private String orderNo;

    private String payOrderNo;

    private String carGroupId;

    private String carTypeId;

    private String carTypeName;

    private String protocolId;

    private String protocolName;

    private Long beginDate;

    private Long endDate;

    private Integer maxRefundAmount;

    private Integer refundAmount;

    private String payChannel;

    private String payType;

    private String userName;

    private String userMobile;

    private String orderState;

    private String projectNo;

    private String creator;

    private String modifier;

    private Timestamp dateCreated;

    private Timestamp dateUpdated;
}
