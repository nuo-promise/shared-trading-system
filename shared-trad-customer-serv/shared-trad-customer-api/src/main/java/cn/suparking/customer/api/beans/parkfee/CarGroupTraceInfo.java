package cn.suparking.customer.api.beans.parkfee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarGroupTraceInfo {

    private Integer leftDay;

    private Integer spaceQuantity;
}
