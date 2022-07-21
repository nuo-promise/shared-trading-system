package cn.suparking.data.api.parkfee;

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
