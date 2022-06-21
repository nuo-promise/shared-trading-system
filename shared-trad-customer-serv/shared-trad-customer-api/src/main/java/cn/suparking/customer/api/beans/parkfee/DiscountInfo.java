package cn.suparking.customer.api.beans.parkfee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscountInfo {
    private String discountNo;

    private String valueType;

    private Integer value;

    private Integer quantity;

    private String usedStartTime;

    private String usedEndTime;
}
