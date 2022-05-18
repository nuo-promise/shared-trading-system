package cn.suparking.user.api.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhoneInfoVO {

    private String phoneNumber;

    private String purePhoneNumber;

    private String countryCode;

    private WaterMarkVO waterMark;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WaterMarkVO {

        private String appid;

        private Long timestamp;
    }
}
