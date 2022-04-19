package cn.suparking.user.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum DeviceStatus {

    UNKNOWN("unknown", 0),
    FREE("空闲", 1),
    NO_FREE("上锁/占位", 2);

    private final String description;

    private final Integer code;

    /**
     * get code desc.
     * @param code  user status code
     * @return user desc
     */
    public static DeviceStatus convert(final Integer code) {
        return Stream.of(values())
                .filter(deviceStatus -> deviceStatus.code.equals(code))
                .findFirst()
                .orElse(UNKNOWN);
    }
}
