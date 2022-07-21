package cn.suparking.customer.api.beans.cargroup;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarGroupDTO {

    /**
     * id.
     */
    private Long id;

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

    private String importNo;

    private String userName;

    private String userMobile;

    private String address;

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
}
