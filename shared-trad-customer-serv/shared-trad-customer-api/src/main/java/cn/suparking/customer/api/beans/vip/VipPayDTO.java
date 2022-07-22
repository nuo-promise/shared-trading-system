package cn.suparking.customer.api.beans.vip;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VipPayDTO {

    // 项目编号
    @NotNull
    @NotBlank
    private String projectNo;

    // 项目名称
    @NotNull
    @NotBlank
    private String projectName;

    // 合约开始时间
    @NotNull
    @NotBlank
    private String beginDate;

    // 合约结束时间
    @NotNull
    @NotBlank
    private String endDate;

    // 用户ID
    @NotNull
    @NotBlank
    private String userId;

    // 用户手机号
    @NotNull
    @NotBlank
    private String phone;

    // 用户 openId
    @NotNull
    @NotBlank
    private String miniOpenId;

    // 库存ID
    @NotNull
    @NotBlank
    private String stockId;

    // 协议ID
    @NotNull
    @NotBlank
    private String protocolId;

    // 协议名称
    @NotNull
    @NotBlank
    private String protocolName;

    // 购买份数
    @NotNull
    @NotBlank
    private Integer quantity;

    // 应付金额
    @NotNull
    @NotBlank
    private Integer dueAmount;
}
