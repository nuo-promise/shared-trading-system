package cn.suparking.data.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscountInfoDO implements Serializable {

    private static final long serialVersionUID = -3799346785207495462L;

    private Long id;

    private Long parkingOrderId;

    private String discountNo;

    private String valueType;

    private Integer value;

    private Integer quantity;

    private String usedStartTime;

    private String usedEndTime;
}
