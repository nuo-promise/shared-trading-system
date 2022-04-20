package cn.suparking.user.dao.entity;

import cn.suparking.user.api.enums.DeviceStatus;
import cn.suparking.user.api.enums.DeviceType;
import cn.suparking.user.api.enums.ParkStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CarParkDO extends BaseDO {

    private Long userId;

    private String parkNo;

    private String address;

    private String parkId;

    private ParkStatus parkStatus;

    private String deviceId;

    private DeviceStatus deviceStatus;

    private DeviceType deviceType;
}
