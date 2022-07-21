package cn.suparking.data.dao.entity.cargroupstocklog;

import cn.suparking.data.dao.entity.BaseDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 合约库存操作表.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CarGroupStockOperateRecordDO extends BaseDO {

    private static final long serialVersionUID = -3263248158752441358L;

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
}
