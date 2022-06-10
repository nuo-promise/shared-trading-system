package cn.suparking.data.dao.entity;

import cn.suparking.common.api.configuration.SnowflakeConfig;
import cn.suparking.data.api.beans.ParkingEventDTO;
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
public class ParkingEventDO extends BaseDO {

    private static final long serialVersionUID = 6317336831992629231L;

    private Long projectId;

    private String eventType;

    private Long eventTime;

    private String deviceNo;

    private String parkId;

    private String parkNo;

    private String parkName;

    private Long recogId;

    private String inSubAreaId;

    private String inSubAreaName;

    private String outSubAreaId;

    private String outSubAreaName;

    private Integer leftDay;

    private Integer spaceQuantity;

    private String operator;

    /**
     * build parking Event DO.
     * @param parkingEventDTO {@link ParkingEventDTO}
     * @return {@link ParkingEventDO}
     */
    public static ParkingEventDO buildParkingEventDO(final ParkingEventDTO parkingEventDTO) {
        return Optional.ofNullable(parkingEventDTO).map(item -> {
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            ParkingEventDO parkingEventDO = ParkingEventDO.builder()
                    .projectId(Long.valueOf(item.getProjectId()))
                    .eventType(item.getEventType())
                    .eventTime(item.getEventTime())
                    .deviceNo(item.getDeviceNo())
                    .parkId(item.getParkId())
                    .parkNo(item.getParkNo())
                    .parkName(item.getParkName())
                    .recogId(Long.valueOf(item.getRecogId()))
                    .inSubAreaId(item.getInSubAreaId())
                    .inSubAreaName(item.getInSubAreaName())
                    .outSubAreaId(item.getOutSubAreaId())
                    .outSubAreaName(item.getOutSubAreaName())
                    .leftDay(item.getLeftDay())
                    .spaceQuantity(item.getSpaceQuantity())
                    .operator(item.getOperator())
                    .build();
            if (Objects.isNull(item.getId())) {
                parkingEventDO.setId(SnowflakeConfig.snowflakeId());
                parkingEventDO.setDateCreated(currentTime);
            } else {
                parkingEventDO.setId(Long.valueOf(item.getId()));
                parkingEventDO.setDateUpdated(currentTime);
            }
            return parkingEventDO;
        }).orElse(null);
    }
}
