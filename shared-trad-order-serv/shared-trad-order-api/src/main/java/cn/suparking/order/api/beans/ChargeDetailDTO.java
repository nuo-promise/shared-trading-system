package cn.suparking.order.api.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChargeDetailDTO implements Serializable {

    private static final long serialVersionUID = 76213029817143448L;

    private String id;

    private String chargeInfoId;

    private String chargeTypeName;

    private Long beginTime;

    private Long endTime;

    private Integer parkingMinutes;

    private Integer balancedMinutes;

    private Integer freeMinutes;

    private Integer discountedMinutes;

    private Integer chargingMinutes;

    private Integer chargeAmount;

    private String remark;
}
