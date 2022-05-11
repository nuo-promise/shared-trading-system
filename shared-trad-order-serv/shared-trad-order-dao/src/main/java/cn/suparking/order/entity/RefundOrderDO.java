package cn.suparking.order.entity;

import api.beans.OtherOrderDTO;
import api.beans.RefundOrderDTO;
import cn.suparking.common.api.configuration.SnowflakeConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefundOrderDO implements Serializable {
    /**
     * 退费订单唯一ID.
     */
    private Long id;

    /**
     * 退费订单号.
     */
    private String refundNo;

    /**
     * 原始订单号.
     */
    private String originOrderNo;

    /**
     * 订单类型 1:本地订单 2:三方订单.
     */
    private String orderType;

    /**
     * 退费人.
     */
    private Long userId;

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

    /**
     * build refundOrderDO.
     *
     * @param refundOrderDTO {@linkplain RefundOrderDTO}
     * @return {@linkplain OtherOrderDO}
     */
    public static RefundOrderDO buildRefundOrderDO(RefundOrderDTO refundOrderDTO) {
        return Optional.ofNullable(refundOrderDTO).map(item -> {
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            RefundOrderDO refundOrderDO = RefundOrderDO.builder()
                    .refundNo(item.getRefundNo())
                    .originOrderNo(item.getOriginOrderNo())
                    .orderType(item.getOrderType())
                    .originAmount(item.getOriginAmount())
                    .refundAmount(item.getRefundAmount())
                    .method(item.getMethod())
                    .status(item.getStatus())
                    .reasons(item.getReasons())
                    .creator(item.getCreator())
                    .modify(item.getModify())
                    .build();

            if (Objects.nonNull(item.getUserId())) {
                refundOrderDO.setUserId(Long.valueOf(item.getUserId()));
            }
            if (Objects.isNull(item.getId())) {
                refundOrderDO.setId(SnowflakeConfig.snowflakeId());
                refundOrderDO.setDateCreated(currentTime);
            } else {
                refundOrderDO.setId(Long.valueOf(item.getId()));
                refundOrderDO.setDateUpdated(currentTime);
            }
            return refundOrderDO;
        }).orElseGet(null);
    }
}
