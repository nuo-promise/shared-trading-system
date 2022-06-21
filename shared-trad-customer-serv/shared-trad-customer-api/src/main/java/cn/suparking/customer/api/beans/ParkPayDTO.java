package cn.suparking.customer.api.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkPayDTO {

    private String tmpOrderNo;

    private String parkingId;

    private String projectNo;

    private String userId;

    private String miniOpenId;
}
