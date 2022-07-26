package cn.suparking.customer.api.beans.cargroup;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarGroupDTO implements Serializable {

    private static final long serialVersionUID = 3664236567892059125L;

    /**
     * id.
     */
    private String id;

    /**
     * 用户id.
     */
    private Long userId;

    /**
     * 项目编号.
     */
    private String projectNo;

    private String carTypeId;

    private String carTypeName;

    private String protocolId;

    private String protocolType;

    private String protocolName;

    private Long beginDate;

    private Long endDate;

    private Integer dueAmount;

    private String payType;

    private String importNo;

    private String userName;

    private String userMobile;

    private String address;

    private Boolean valid;

    private String remark;

    private String operator;

    private String modifier;

    /**
     * 创建时间.
     */
    private Timestamp dateCreated;

    /**
     * 更新时间.
     */
    private Timestamp dateUpdated;

    private List<CarGroupPeriodDTO> carGroupPeriodDTOList;
}
