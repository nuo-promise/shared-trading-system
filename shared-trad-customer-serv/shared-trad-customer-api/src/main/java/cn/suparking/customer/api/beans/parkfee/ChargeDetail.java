package cn.suparking.customer.api.beans.parkfee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChargeDetail {

    private String chargeTypeName;

    private Long beginTime;

    private Long endTime;

    private Integer parkingMinutes;

    private Integer balancedMinutes;

    private Integer freedMinutes;

    private Integer discountedMinutes;

    private Integer chargingMinutes;

    private Integer chargeAmount = 0;

    private String remark;
}
