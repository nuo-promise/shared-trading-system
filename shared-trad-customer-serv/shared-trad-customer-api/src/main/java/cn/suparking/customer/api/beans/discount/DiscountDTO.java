package cn.suparking.customer.api.beans.discount;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscountDTO {

    private String projectNo;

    private String discountNo;
}
