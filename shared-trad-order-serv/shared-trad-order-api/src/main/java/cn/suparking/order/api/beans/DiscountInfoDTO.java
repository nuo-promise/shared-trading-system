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
public class DiscountInfoDTO implements Serializable {

    private static final long serialVersionUID = -8949602346357239809L;

    private String id;

    private String parkingOrderId;

    private String discountNo;

    private String valueType;

    private Integer value;

    private Integer quantity;

    private String usedStartTime;

    private String usedEndTime;
}
