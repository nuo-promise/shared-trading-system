package cn.suparking.data.dao.entity;

import cn.suparking.common.api.configuration.SnowflakeConfig;
import cn.suparking.data.api.beans.ParkingTriggerDTO;
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
public class ParkingTriggerDO extends BaseDO {

    private static final long serialVersionUID = -6292785087610775485L;

    private Long projectId;

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
     * build parking Trigger DO.
     * @param parkingTriggerDTO {@link ParkingTriggerDTO}
     * @return {@link ParkingTriggerDO}
     */
    public static ParkingTriggerDO buildParkingTriggerDO(final ParkingTriggerDTO parkingTriggerDTO) {
        return Optional.ofNullable(parkingTriggerDTO).map(item -> {
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            ParkingTriggerDO parkingTriggerDO = ParkingTriggerDO.builder()
                    .projectId(Long.valueOf(item.getProjectId()))
                    .recogTime(item.getRecogTime())
                    .openTime(item.getOpenTime())
                    .deviceNo(item.getDeviceNo())
                    .parkId(item.getParkId())
                    .parkNo(item.getParkNo())
                    .parkName(item.getParkName())
                    .inSubAreaId(item.getInSubAreaId())
                    .inSubAreaName(item.getInSubAreaName())
                    .outSubAreaId(item.getOutSubAreaId())
                    .outSubAreaName(item.getOutSubAreaName())
                    .carTypeId(item.getCarTypeId())
                    .carTypeName(item.getCarTypeName())
                    .leftDay(item.getLeftDay())
                    .spaceQuantity(item.getSpaceQuantity())
                    .operator(item.getOperator())
                    .remark(item.getRemark())
                    .build();
            if (Objects.isNull(item.getId())) {
                parkingTriggerDO.setId(SnowflakeConfig.snowflakeId());
                parkingTriggerDO.setDateCreated(currentTime);
            } else {
                parkingTriggerDO.setId(Long.valueOf(item.getId()));
                parkingTriggerDO.setDateUpdated(currentTime);
            }
            return parkingTriggerDO;
        }).orElse(null);
    }
}
