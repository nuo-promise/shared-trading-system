package cn.suparking.data.api.beans.cargroupstocklog;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CarGroupStockOperateRecordQueryDTO {

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
     * 页数.
     */
    @NotNull(message = "请选择页码")
    @Min(value = 1, message = "页码最小值为1")
    private Integer page;

    /**
     * 每页条数.
     */
    @NotNull(message = "请选择每页显示数量")
    @Min(value = 1, message = "每页最少显示1条")
    private Integer size;
}
