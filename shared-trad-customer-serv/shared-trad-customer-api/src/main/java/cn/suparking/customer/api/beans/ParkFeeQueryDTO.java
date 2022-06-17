package cn.suparking.customer.api.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkFeeQueryDTO extends ParkBaseQueryDTO {

    /**
     * 查询设备编号.
     */
    private String deviceNo;
}
