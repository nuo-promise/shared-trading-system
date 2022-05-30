package api.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkingRefundOrderDTO implements Serializable {

    private static final long serialVersionUID = 7265354678951677352L;

    private String id;

    private String userId;

    @NotNull
    @NotBlank
    private String orderNo;

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
}
