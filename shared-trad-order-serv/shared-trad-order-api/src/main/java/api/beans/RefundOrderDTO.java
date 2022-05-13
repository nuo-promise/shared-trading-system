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
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefundOrderDTO implements Serializable {
    /**
     * 退费订单唯一ID.
     */
    private String id;

    /**
     * 退费订单号.
     */
    @NotNull
    @NotBlank
    private String refundNo;

    /**
     * 原始订单号.
     */
    @NotNull
    @NotBlank
    private String originOrderNo;

    /**
     * 订单类型 1:本地订单 2:三方订单.
     */
    private String orderType;

    /**
     * 退费人.
     */
    private String userId;

    /**
     * 原始订单金额.
     */
    private Long originAmount;

    /**
     * 退费金额.
     */
    private Long refundAmount;

    /**
     * 退费方式 1:原路返回 2:手动.
     */
    private Integer method;

    /**
     * 退费状态 0:成功 1:正在处理 2:退费失败.
     */
    private Integer status;

    /**
     * 失败原因.
     */
    private String reasons;

    /**
     * 创建时间.
     */
    private Timestamp dateCreated;

    /**
     * 更新时间.
     */
    private Timestamp dateUpdated;

    /**
     * 创建者.
     */
    private String creator;

    /**
     * 更新者.
     */
    private String modify;
}
