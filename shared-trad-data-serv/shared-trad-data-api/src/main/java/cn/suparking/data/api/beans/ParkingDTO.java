package cn.suparking.data.api.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.LinkedList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkingDTO implements Serializable {

    private static final long serialVersionUID = 7212486128659989912L;

    private String id;

    private String userId;

    private String projectId;

    private String parkId;

    private String parkNo;

    private String parkName;

    private String deviceNo;

    private String carGroupId;

    private String specialType;

    private Long enter;

    private Long leave;

    private LinkedList<String> parkingEvents;

    private Long firstEnterTriggerTime;

    private Long latestTriggerTime;

    private String latestTriggerParkId;

    private Integer latestTriggerTemp;

    private String latestTriggerTypeClass;

    private String latestTriggerTypeName;

    private String parkingState;

    private String abnormalReason;

    private Integer numberOfNight;

    private Integer allowCorrect;

    private Long matchedParkingId;

    private Integer valid;

    private Long pendingOrder;

    private String payParkingId;

    private Integer parkingMinutes;

    private String projectNo;

    private String remark;

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
