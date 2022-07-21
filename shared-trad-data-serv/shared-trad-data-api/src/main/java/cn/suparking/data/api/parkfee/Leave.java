package cn.suparking.data.api.parkfee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Leave {

    private String recogId;

    //车辆驶入时间, 设备上报时间
    private Long recogTime;

    // 常升 降锁成功时间, 常降 升锁成功时间
    private Long openTime;

    private String deviceNo;

    // 车位ID
    private String parkId;

    // 车位编号
    private String parkNo;

    // 车位名称
    private String parkName;

    private String inSubAreaId;

    private String inSubAreaName;

    private String outSubAreaId;

    private String outSubAreaName;

    private String carTypeId;

    private String carTypeName;

    private CarGroupTraceInfo carGroupTraceInfo;

    private String operator;

    private String remark;
}
