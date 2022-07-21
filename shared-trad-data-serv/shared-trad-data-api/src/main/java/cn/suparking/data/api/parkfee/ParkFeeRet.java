package cn.suparking.data.api.parkfee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkFeeRet {
    private String code;

    private String msg;

    // 暂存临时订单
    private String tmpOrderNo;

    private Parking parking;

    private ParkingOrder parkingOrder;
}
