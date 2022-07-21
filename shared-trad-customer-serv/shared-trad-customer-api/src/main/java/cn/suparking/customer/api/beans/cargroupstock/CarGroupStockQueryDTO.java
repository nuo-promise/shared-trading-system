package cn.suparking.customer.api.beans.cargroupstock;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 合约协议库存查询条件实体.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarGroupStockQueryDTO {
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

    /**
     * 库存id.
     */
    private Long id;

    /**
     * 车场编号.
     */
    private List<String> projectNos;

    /**
     * 合约协议id.
     */
    private String protocolId;

    /**
     * 操作人.
     */
    private String modifier;

    /**
     * 操作类型 (INCREASE 增加库存 / DECREASE 缩减库存).
     */
    private String operateType;

    /**
     * 操作数量.
     */
    private Integer quantity;

    /**
     * 操作备注.
     */
    private String remark;
}
