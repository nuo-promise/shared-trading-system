package cn.suparking.customer.api.beans.cargroupstock;

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
     * 用户库存ID.
     */
    private Long stockId;

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
