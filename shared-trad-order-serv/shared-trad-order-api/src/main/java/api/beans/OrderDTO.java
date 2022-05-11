package api.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 订单信息.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO implements Serializable {
    /**
     * 我的订单唯一ID.
     */
    private String id;

    /**
     * 买家ID.
     */
    private String buyerId;

    /**
     * 卖家ID.
     */
    private String sellerId;

    /**
     * 订单号.
     */
    @NotNull
    @NotBlank
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
}
