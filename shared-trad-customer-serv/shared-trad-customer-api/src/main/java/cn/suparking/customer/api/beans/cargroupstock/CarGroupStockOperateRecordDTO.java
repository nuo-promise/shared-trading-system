package cn.suparking.customer.api.beans.cargroupstock;

import cn.suparking.common.api.configuration.SnowflakeConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CarGroupStockOperateRecordDTO implements Serializable {

    private static final long serialVersionUID = -1329226252137138071L;
    /**
     * 唯一雪花ID.
     */
    private Long id;

    /**
     * 用户库存ID.
     */
    private Long stockId;

    /**
     * 操作类型  增加库存:INCREASE 缩减库存:DECREASE 消费库存:CONSUME 返库:返库.
     */
    private String operateType;

    /**
     * 操作数量.
     */
    private Integer quantity;

    /**
     * 操作终端.
     */
    private String termNo;

    /**
     * 备注.
     */
    private String remark;

    /**
     * 创建者.
     */
    private String creator;

    /**
     * 更新者.
     */
    private String modifier;

    /**
     * 创建日期.
     */
    private Timestamp dateCreated;

    /**
     * 更新日期.
     */
    private Timestamp dateUpdated;

    /**
     * 记录入库操作.
     *
     * @param stockId  用户库存ID
     * @param quantity 操作数量
     * @param creator  创建者
     * @param termNo   操作终端
     * @param remark   备注
     * @return {@linkplain CarGroupStockOperateRecordDTO}
     */
    public CarGroupStockOperateRecordDTO logIn(final Long stockId, final Integer quantity, final String creator, final String termNo, final String remark) {
        this.id = SnowflakeConfig.snowflakeId();
        this.stockId = stockId;
        this.operateType = "INCREASE";
        this.quantity = quantity;
        this.creator = creator;
        this.termNo = termNo;
        this.dateCreated = new Timestamp(System.currentTimeMillis());
        this.remark = remark;
        return this;
    }

    /**
     * 记录减库操作(指人为缩库操作).
     *
     * @param stockId  用户库存ID
     * @param quantity 操作数量
     * @param creator  创建者
     * @param termNo   操作终端
     * @param remark   备注
     * @return {@linkplain CarGroupStockOperateRecordDTO}
     */
    public CarGroupStockOperateRecordDTO logOut(final Long stockId, final Integer quantity, final String creator, final String termNo, final String remark) {
        this.id = SnowflakeConfig.snowflakeId();
        this.stockId = stockId;
        this.operateType = "DECREASE";
        this.quantity = quantity;
        this.creator = creator;
        this.termNo = termNo;
        this.dateCreated = new Timestamp(System.currentTimeMillis());
        this.remark = remark;
        return this;
    }

    /**
     * 记录消费减库操作(指消费减库).
     *
     * @param stockId  用户库存ID
     * @param quantity 操作数量
     * @param creator  创建者
     * @param termNo   操作终端
     * @param remark   备注
     * @return {@linkplain CarGroupStockOperateRecordDTO}
     */
    public CarGroupStockOperateRecordDTO logConsume(final Long stockId, final Integer quantity, final String creator, final String termNo, final String remark) {
        this.id = SnowflakeConfig.snowflakeId();
        this.stockId = stockId;
        this.operateType = "CONSUME";
        this.quantity = quantity;
        this.creator = creator;
        this.termNo = termNo;
        this.dateCreated = new Timestamp(System.currentTimeMillis());
        this.remark = remark;
        return this;
    }

    /**
     * 记录返库操作.
     *
     * @param stockId  用户库存ID
     * @param quantity 操作数量
     * @param creator  创建者
     * @param termNo   操作终端
     * @param remark   备注
     * @return {@linkplain CarGroupStockOperateRecordDTO}
     */
    public CarGroupStockOperateRecordDTO logReturn(final Long stockId, final Integer quantity, final String creator, final String termNo, final String remark) {
        this.id = SnowflakeConfig.snowflakeId();
        this.stockId = stockId;
        this.operateType = "RETURN";
        this.quantity = quantity;
        this.creator = creator;
        this.termNo = termNo;
        this.dateCreated = new Timestamp(System.currentTimeMillis());
        this.remark = remark;
        return this;
    }
}
