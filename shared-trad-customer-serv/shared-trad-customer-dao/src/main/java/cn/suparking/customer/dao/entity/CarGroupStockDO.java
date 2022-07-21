package cn.suparking.customer.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

/**
 * 合约库存表.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CarGroupStockDO extends BaseDO {

    private static final long serialVersionUID = 8487828821835768840L;

    /**
     * 车场编号.
     */
    private String projectNo;

    /**
     * 车场名称.
     */
    private String projectName;

    /**
     * 协议ID.
     */
    private String protocolId;

    /**
     * 协议名称.
     */
    private String protocolName;

    /**
     * 库存类型.
     */
    private String stockType;

    /**
     * 库存数量.
     */
    private Integer stockQuantity;

    /**
     * 累计入库.
     */
    private Integer cumulativeIn;

    /**
     * 累计出库.
     */
    private Integer cumulativeOut;

    /**
     * 累计反库.
     */
    private Integer cumulativeReturn;

    /**
     * 创建者.
     */
    private String creator;

    /**
     * 更新者.
     */
    private String modifier;

    /**
     * 入库操作(累加总库存 + 累加累积入库数).
     *
     * @param quantity 操作数量
     * @param operator 操作者
     * @return {@linkplain CarGroupStockDO}
     */
    public CarGroupStockDO doIn(Integer quantity, String operator) {
        this.stockQuantity = this.stockQuantity + quantity;
        this.cumulativeIn = this.cumulativeIn + quantity;
        this.modifier = operator;
        this.setDateUpdated(new Timestamp(System.currentTimeMillis()));
        return this;
    }

    /**
     * 出库操作(减少总库存 + 累加累积出库数).
     *
     * @param quantity 操作数量
     * @param operator 操作者
     * @return {@linkplain CarGroupStockDO}
     */
    public CarGroupStockDO doOut(Integer quantity, String operator) {
        int number = this.stockQuantity - quantity;
        if (number <= 0) {
            this.stockQuantity = 0;
        } else {
            this.stockQuantity = number;
        }
        this.cumulativeOut = this.cumulativeOut + quantity;
        this.modifier = operator;
        this.setDateUpdated(new Timestamp(System.currentTimeMillis()));
        return this;
    }

    /**
     * 返库操作(累加总库存 + 累加累积返库数).
     *
     * @param quantity 操作数量
     * @param operator 操作者
     * @return {@linkplain CarGroupStockDO}
     */
    public CarGroupStockDO doReturn(Integer quantity, String operator) {
        this.stockQuantity = this.stockQuantity + quantity;
        this.cumulativeReturn = this.cumulativeReturn + quantity;
        this.modifier = operator;
        this.setDateUpdated(new Timestamp(System.currentTimeMillis()));
        return this;
    }
}
