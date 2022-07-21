package cn.suparking.data.vo.parking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkingVo {
    private Long id;

    private String iphone;

    private Integer parkingMinutes;

    private String latestTriggerTypeName;

    private String parkingState;

    private Long latestTriggerTime;

    private String projectNo;

    private String latestTriggerParkId;
}
