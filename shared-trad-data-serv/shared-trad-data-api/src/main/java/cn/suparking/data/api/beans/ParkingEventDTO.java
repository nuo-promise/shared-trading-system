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
public class ParkingEventDTO implements Serializable {

    private static final long serialVersionUID = 5620939087526603858L;

    private String id;

    private String projectId;

    private String eventType;

    private Long eventTime;

    private String deviceNo;

    private String parkId;

    private String parkNo;

    private String parkName;

    private String recogId;

    private String inSubAreaId;

    private String inSubAreaName;

    private String outSubAreaId;

    private String outSubAreaName;

    private Integer leftDay;

    private Integer spaceQuantity;

    private String operator;

    /**
     * 创建时间.
     */
    private Timestamp dateCreated;

    /**
     * 更新时间.
     */
    private Timestamp dateUpdated;
}
