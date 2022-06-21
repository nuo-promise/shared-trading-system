package cn.suparking.customer.api.beans.parkfee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChargeInfo {
    private Integer beginCycleSeq;

    private Integer cycleNumber;

    private LinkedList<ChargeDetail> chargeDetails;

    private Integer parkingMinutes;

    private Integer balancedMinutes;

    private Integer discountedMinutes;

    private Integer totalAmount;

    private Integer extraAmount;
}
