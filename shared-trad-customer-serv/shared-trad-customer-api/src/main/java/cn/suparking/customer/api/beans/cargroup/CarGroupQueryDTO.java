package cn.suparking.customer.api.beans.cargroup;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarGroupQueryDTO {
    /**
     * 用户id.
     */
    private Long userId;

    /**
     * 当前操作用户.
     */
    private String LoginUserName;

    /**
     * 项目编号.
     */
    private List<String> projectNos;

    /**
     * 手机号.
     */
    private String userMobile;

    /**
     * 车辆类型数组.
     */
    private List<String> carTypeIds;

    /**
     * 租约协议数组.
     */
    private List<String> protocolIds;

    /**
     * 协议类型.
     */
    private String protocolType;

    /**
     * 当前时间.
     */
    private Long nowTime;

    /**
     * 合约状态.
     */
    private String state;

    /**
     * 页码.
     */
    @NotNull(message = "请选择页码")
    @Min(value = 1,message = "页码最小值为1")
    private Integer pageNum;

    /**
     * 每页显示数量.
     */
    @NotNull(message = "请选择每页显示数量")
    @Min(value = 1,message = "每页最少显示1条")
    private Integer pageSize;
}
