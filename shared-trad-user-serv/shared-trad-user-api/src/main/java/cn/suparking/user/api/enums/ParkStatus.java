package cn.suparking.user.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum ParkStatus {
    UNKNOWN("unknown", "0"),
    FREE("空闲", "1"),
    REVERSE("发布中", "2"),
    REVERSED("被预约", "3"),
    ENTERED("已驶入", "4"),
    LEFT("已驶离", "5");

    private final String description;

    private final String code;

    /**
     * get desc by code.
     * @param code  user register code
     * @return user register desc
     */
    public static ParkStatus convert(final String code) {
        return Stream.of(values())
                .filter(parkStatus -> parkStatus.code.equalsIgnoreCase(code))
                .findFirst()
                .orElse(UNKNOWN);
    }
}
