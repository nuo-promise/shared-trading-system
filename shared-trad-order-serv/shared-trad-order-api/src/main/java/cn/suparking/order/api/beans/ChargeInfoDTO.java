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
public class ChargeInfoDTO implements Serializable {

    private static final long serialVersionUID = -2391339154992314213L;

    private String id;

    private String parkingOrderId;

    private Integer beginCycleSeq;

    private Integer cycleNumber;

    private Integer parkingMinutes;

    private Integer balancedMinutes;

    private Integer discountedMinutes;

    private Integer totalAmount;

    private Integer extraAmount;
}
