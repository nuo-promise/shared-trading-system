package cn.suparking.customer.api.beans.parkfee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Parking {

    // 停车记录id
    private String id;

    private Long userId;

    // 车位ID
    private String parkId;

    // 车位编号,车位名称
    private String parkNo;

    private String parkName;

    // 设备编号
    private String deviceNo;

    private String carGroupId;

    private String specialType;

    private Enter enter;

    private Leave leave;

    private Long firstEnterTriggerTime;

    private Long latestTriggerTime;

    private String latestTriggerParkId;

    private Boolean latestTriggerTemp;

    private String latestTriggerTypeClass;

    private String latestTriggerTypeName;

    private LinkedList<ParkingEvent> parkingEvents = new LinkedList<>();

    private String parkingState;

    private String abnormalReason;

    private Integer numberOfNight;

    private Boolean allowCorrect;

    private String matchedParkingId;

    private Boolean valid;

    private ParkingOrder pendingOrder;

    private String payParkingId;

    private Integer parkingMinutes;

    private String remark;

    private String projectNo;

    private ParkingConfig parkingConfig;

    private String creator;

    private Long createTime;

    private String modifier;

    private Long modifyTime;
}
