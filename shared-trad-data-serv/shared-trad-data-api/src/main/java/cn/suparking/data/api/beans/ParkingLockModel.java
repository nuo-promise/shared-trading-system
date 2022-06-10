package cn.suparking.data.api.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkingLockModel {

    // 车位 id
    private String id;

    // 车位 编号
    private String parkNo;

    // 车位 ID
    private String parkId;

    // 车位名称
    private String parkName;

    // 设备编号
    private String deviceNo;

    // 充电桩 编号
    private String chargeNo;

    // 设备类型 MOVEBOARD TYPARK CTP
    private String deviceType;

    // 降锁无车检测时长
    private Integer detectTime;

    //是否可用 true false
    private Boolean enabled;

    private String remark;

    private String projectNo;

    // 车位 雪花 ID
    private String projectId;
}
