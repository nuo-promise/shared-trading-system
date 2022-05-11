package cn.suparking.order.entity;

import api.beans.OrderDTO;
import cn.suparking.common.api.configuration.SnowflakeConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Optional;

/**
 * 订单信息.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDO implements Serializable {
    /**
     * 我的订单唯一ID.
     */
    private Long id;

    /**
     * 买家ID.
     */
    private Long buyerId;

    /**
     * 卖家ID.
     */
    private Long sellerId;

    /**
     * 订单号.
     */
    private String orderNo;

    /**
     * 订单类型 1:行程订单 2:充值订单.
     */
    private Integer orderType;

    /**
     * 订单状态 1:已完成 2:进行中 3:已关闭 4:已取消.
     */
    private Integer orderStatus;

    /**
     * 行程开始时间/开始充值时间.
     */
    private Timestamp dateBegin;

    /**
     * 行程结束时间/结束充值时间.
     */
    private Timestamp dateEnd;

    /**
     * 订单耗时 秒.
     */
    private Integer orderMinutes;

    /**
     * 平台订单金额.
     */
    private Long amount;

    /**
     * 平台收取的佣金 分.
     */
    private Long platformAmount;

    /**
     * 平台佣金费率 %.
     */
    private Integer platformRate;

    /**
     * 使用优惠券ID.
     */
    private String discountId;

    /**
     * 第三方订单数组字符串.
     */
    private String otherOrderIds;

    /**
     * 开票状态 0:未开票 1:线上开票 2:手动开票.
     */
    private Integer invoiceStatus;

    /**
     * 订单备注.
     */
    private String remark;

    /**
     * 创建时间.
     */
    private Timestamp dateCreated;

    /**
     * 更新时间.
     */
    private Timestamp dateUpdated;

    /**
     * build orderDO.
     *
     * @param orderDTO {@linkplain OrderDTO}
     * @return OrderDO
     */
    public static OrderDO buildOrderDO(final OrderDTO orderDTO) {
        return Optional.ofNullable(orderDTO).map(item -> {
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            OrderDO orderDO = OrderDO.builder()
                    .buyerId(Long.valueOf(item.getBuyerId()))
                    .sellerId(Long.valueOf(item.getSellerId()))
                    .orderNo(item.getOrderNo())
                    .orderType(item.getOrderType())
                    .orderStatus(item.getOrderStatus())
                    .dateBegin(item.getDateBegin())
                    .dateEnd(item.getDateEnd())
                    .orderMinutes(item.getOrderMinutes())
                    .amount(item.getAmount())
                    .platformAmount(item.getPlatformAmount())
                    .platformRate(item.getPlatformRate())
                    .discountId(item.getDiscountId())
                    .otherOrderIds(item.getOtherOrderIds())
                    .remark(item.getRemark())
                    .build();

            if (Objects.nonNull(item.getBuyerId())) {
                orderDO.setBuyerId(Long.valueOf(item.getBuyerId()));
            }
            if (Objects.nonNull(item.getSellerId())) {
                orderDO.setSellerId(Long.valueOf(item.getSellerId()));
            }
            if (Objects.isNull(item.getId())) {
                orderDO.setId(SnowflakeConfig.snowflakeId());
                orderDO.setDateCreated(currentTime);
                orderDO.setInvoiceStatus(0);
            } else {
                orderDO.setId(Long.valueOf(item.getId()));
                orderDO.setDateUpdated(currentTime);
            }
            return orderDO;
        }).orElseGet(null);
    }
}
