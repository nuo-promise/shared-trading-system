package cn.suparking.order.api.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkingRefundOrderQueryDTO implements Serializable {

    private static final long serialVersionUID = 7265354678951677352L;

    private String id;

    private Long userId;

    private String orderNo;

    private String payOrderNo;

    private String payParkingId;

    private Integer maxRefundAmount;

    private Integer refundAmount;

    private String payChannel;

    private String payType;

    private String orderState;

    private String projectNo;

    private String creator;

    private String modifier;

    private Timestamp dateCreated;

    private Timestamp dateUpdated;

    private List<String> orderStateList;
}
