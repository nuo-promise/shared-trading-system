package cn.suparking.data.dao.entity;

import cn.suparking.common.api.configuration.SnowflakeConfig;
import cn.suparking.data.api.beans.ParkingDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ParkingDO extends BaseDO {
    private static final long serialVersionUID = -5296979911048492463L;

    private Long userId;

    private Long projectId;

    private String parkId;

    private String parkNo;

    private String parkName;

    private String deviceNo;

    private String carGroupId;

    private String specialType;

    private Long enter;

    private Long leave;

    private String parkingEvents;

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
     * build parking DO.
     * @param parkingDTO {@link ParkingDTO}
     * @return {@link ParkingDO}
     */
    public static ParkingDO buildParkingDO(final ParkingDTO parkingDTO) {
        return Optional.ofNullable(parkingDTO).map(item -> {
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            ParkingDO parkingDO = ParkingDO.builder()
                    .projectId(Long.valueOf(item.getProjectId()))
                    .parkId(item.getParkId())
                    .parkNo(item.getParkNo())
                    .parkName(item.getParkName())
                    .deviceNo(item.getDeviceNo())
                    .carGroupId(item.getCarGroupId())
                    .specialType(item.getSpecialType())
                    .enter(item.getEnter())
                    .leave(item.getLeave())
                    .parkingEvents(String.join(",", item.getParkingEvents()))
                    .firstEnterTriggerTime(item.getFirstEnterTriggerTime())
                    .latestTriggerTime(item.getLatestTriggerTime())
                    .latestTriggerParkId(item.getLatestTriggerParkId())
                    .latestTriggerTemp(item.getLatestTriggerTemp())
                    .latestTriggerTypeClass(item.getLatestTriggerTypeClass())
                    .latestTriggerTypeName(item.getLatestTriggerTypeName())
                    .parkingState(item.getParkingState())
                    .abnormalReason(item.getAbnormalReason())
                    .numberOfNight(item.getNumberOfNight())
                    .allowCorrect(item.getAllowCorrect())
                    .matchedParkingId(item.getMatchedParkingId())
                    .valid(item.getValid())
                    .pendingOrder(item.getPendingOrder())
                    .payParkingId(item.getPayParkingId())
                    .parkingMinutes(item.getParkingMinutes())
                    .remark(item.getRemark())
                    .projectNo(item.getProjectNo())
                    .creator(item.getCreator())
                    .modifier(item.getModifier())
                    .build();
            if (Objects.isNull(item.getId())) {
                parkingDO.setId(SnowflakeConfig.snowflakeId());
                parkingDO.setDateCreated(currentTime);
            } else {
                parkingDO.setId(Long.valueOf(item.getId()));
                parkingDO.setDateUpdated(currentTime);
            }
            return parkingDO;
        }).orElse(null);
    }
}
