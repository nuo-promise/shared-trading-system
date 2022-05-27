package cn.suparking.customer.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public final class CarGroup extends BaseDO {

    private static final long serialVersionUID = -1537990665386430042L;

    private Long userId;

    private String projectNo;

    private String carTypeId;

    private String carTypeName;

    private String protocolId;

    private String protocolType;

    private String protocolName;

    private Long beginDate;

    private Long endDate;

    private String importNo;

    private String userName;

    private String userMobile;

    private String address;

    private String remark;

    private String creator;

    private String modifier;
}
