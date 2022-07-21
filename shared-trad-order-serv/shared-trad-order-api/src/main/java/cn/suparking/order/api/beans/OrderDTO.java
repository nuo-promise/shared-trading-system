package cn.suparking.order.api.beans;

import cn.suparking.data.api.parkfee.ParkingOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    @NotNull
    private ParkingOrder parkingOrder;

    private String orderNo;

    @NotNull
    @NotBlank
    private String payType;

    @NotNull
    @NotBlank
    private String termNo;

    @NotNull
    private Integer amount;

    @NotNull
    @NotBlank
    private String plateForm;

    private Long payTime;
}
