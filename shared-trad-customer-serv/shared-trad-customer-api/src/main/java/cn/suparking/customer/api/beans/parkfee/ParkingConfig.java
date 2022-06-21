package cn.suparking.customer.api.beans.parkfee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkingConfig {

    private Integer txTTL;

    private Integer prepayFreeMinutes;

    private Integer bindDiscountFreeMinutes;

    private Integer minIntervalForDupPark;
}
