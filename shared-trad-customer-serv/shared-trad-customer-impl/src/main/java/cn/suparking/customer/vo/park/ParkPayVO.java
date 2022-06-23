package cn.suparking.customer.vo.park;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkPayVO {
    // 场库名称
    private String parkName;

    // 场库编号
    private String projectNo;

    // 开放时间
    private String openTime;

    // 免费分钟数.
    private Integer freeMinutes;

    // 一天封顶金额
    private Integer perCharge;

    // 计费规则大概描述
    private String chargeContent;
}
