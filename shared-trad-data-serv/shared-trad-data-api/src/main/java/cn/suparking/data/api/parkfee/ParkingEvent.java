package cn.suparking.data.api.parkfee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkingEvent {

    private String eventType;

    private Long eventTime;

    private String parkId;

    private String parkNo;

    private String deviceNo;

    private String parkName;

    private String recogId;

    private String inSubAreaId;

    private String inSubAreaName;

    private String outSubAreaId;

    private String outSubAreaName;

    private CarGroupTraceInfo carGroupTraceInfo;
}
