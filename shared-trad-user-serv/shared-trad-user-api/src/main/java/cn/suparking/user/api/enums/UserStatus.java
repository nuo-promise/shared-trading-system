package cn.suparking.user.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * User Status Enum.
 */
@Getter
@AllArgsConstructor
public enum UserStatus {

    UNKNOWN("unknown", 0),
    ACTIVE("激活", 1),
    NO_ACTIVE("未激活", 2);

    private final String description;

    private final Integer code;

    /**
     * get code desc.
     * @param code  user status code
     * @return user desc
     */
    public static UserStatus convert(final Integer code) {
        return Stream.of(values())
                .filter(userStatus -> userStatus.code.equals(code))
                .findFirst()
                .orElse(UNKNOWN);
    }
}
