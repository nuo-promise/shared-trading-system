package cn.suparking.order.api.beans;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarGroupRefundOrderQueryDTO {
    /**
     * 用户id.
     */
    private Long userId;

    /**
     * 当前操作用户.
     */
    private String LoginUserName;

    /**
     * 搜索关键字.
     */
    private String keyword;

    /**
     * 项目编号.
     */
    private List<String> projectNos;

    /**
     * 订单号.
     */
    private String orderNo;

    /**
     * 退费方式.
     */
    private String payType;

    /**
     * 原订单号.
     */
    private String payOrderNo;

    /**
     * 开始时间.
     */
    private Long beginTime;

    /**
     * 结束时间.
     */
    private Long endTime;

    /**
     * 订单状态.
     */
    private String orderState;

    /**
     * 页码.
     */
    @NotNull(message = "请选择页码")
    @Min(value = 1, message = "页码最小值为1")
    private Integer pageNum;

    /**
     * 每页显示数量.
     */
    @NotNull(message = "请选择每页显示数量")
    @Min(value = 1, message = "每页最少显示1条")
    private Integer pageSize;
}
