package cn.suparking.customer.dao.vo.cargroup;

import cn.suparking.customer.dao.entity.CarGroupPeriod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyVipCarVo {
    /**
     * 合约id.
     */
    private String id;

    /**
     * 用户id.
     */
    private String userId;

    /**
     * 车场编号.
     */
    private String projectNo;

    /**
     * 车场名称.
     */
    private String projectName;

    /**
     * 车辆类型名称.
     */
    private String carTypeName;

    /**
     * 合约协议id.
     */
    private String protocolId;

    /**
     * 合约协议名称.
     */
    private String protocolName;

    /**
     * 合约描述.
     */
    private String protocolDesc;

    /**
     * 合约截止日期(如果有则表示生效中, 如果没有则表示已过期).
     */
    private Long endDate;

    /**
     * 是否可线上续费.
     */
    private Boolean canRenew = false;

    /**
     * 合约未生效时间段.
     */
    private List<CarGroupPeriod> futureList;
}
