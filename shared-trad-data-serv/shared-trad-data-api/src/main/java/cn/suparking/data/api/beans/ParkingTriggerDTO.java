package cn.suparking.data.api.beans;

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
public class ParkingTriggerDTO implements Serializable {

    private static final long serialVersionUID = 5078706400610196748L;

    private String id;

    private String projectId;

    private Long recogTime;

    private Long openTime;

    private String deviceNo;

    private String parkId;

    private String parkName;

    private String parkNo;

    private String inSubAreaId;

    private String inSubAreaName;

    private String outSubAreaId;

    private String outSubAreaName;

    private String carTypeId;

    private String carTypeName;

    private Integer leftDay;

    private Integer spaceQuantity;

    private String operator;

    private String remark;

    /**
     * 创建时间.
     */
    private Timestamp dateCreated;

    /**
     * 更新时间.
     */
    private Timestamp dateUpdated;

}
