package api.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 三方订单.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtherOrderDTO implements Serializable {
    /**
     * 第三方订单唯一ID.
     */
    private String id;

    /**
     * 第三方订单编号.
     */
    private String orderNo;

    /**
     * 订单类型 1: 停车 2:充电桩 3:地锁 4:洗车 5:其他.
     */
    private Integer type;

    /**
     * 开始时间.
     */
    private Timestamp dateBegin;

    /**
     * 结束时间.
     */
    private Timestamp dateEnd;

    /**
     * 第三方平台方.
     */
    private Integer platform;

    /**
     * 交易总金额.
     */
    private Long amount;

    /**
     * 应收金额.
     */
    private Long dueAmount;

    /**
     * 实收金额.
     */
    private Long receiveAmount;

    /**
     * 优惠方式: 0:无 1:金额 2:时长 3:折扣 4:全免.
     */
    private Integer discountType;

    /**
     * 优惠数值.
     */
    private Long discountValue;

    /**
     * 备注.
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
