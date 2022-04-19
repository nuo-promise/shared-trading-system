package cn.suparking.user.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * User Register Type.
 */
@Getter
@AllArgsConstructor
public enum RegisterType {

    UNKNOWN("unknown", 0),
    IPHONE("手机端", 1),
    WECHAT_MINI("小程序", 2),
    ALI_MINI("支付宝", 3),
    OTHER("其他", 4);

    private final String description;

    private final Integer code;

    /**
     * get desc by code.
     * @param code  user register code
     * @return user register desc
     */
    public static RegisterType convert(final Integer code) {
        return Stream.of(values())
                .filter(registerType -> registerType.code.equals(code))
                .findFirst()
                .orElse(UNKNOWN);
    }
}
