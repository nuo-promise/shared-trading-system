package cn.suparking.user.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum DeviceType {

    UNKNOWN("unknown", 0),
    LOCK("地锁", 1),
    GEOMAGNETIC("地磁", 2);

    private final String description;

    private final Integer code;

    /**
     * get code desc.
     * @param code  user status code
     * @return user desc
     */
    public static DeviceType convert(final Integer code) {
        return Stream.of(values())
                .filter(deviceType -> deviceType.code.equals(code))
                .findFirst()
                .orElse(UNKNOWN);
    }
}
