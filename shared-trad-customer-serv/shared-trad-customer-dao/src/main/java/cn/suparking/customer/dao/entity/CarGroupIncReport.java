package cn.suparking.customer.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public final class CarGroupIncReport extends BaseDO {

    private static final long serialVersionUID = 5177871335178367693L;

    private String reportDate;

    private Long reportDateForSeconds;

    private Integer todayAdd;

    private Integer todayExpire;

    private Integer todayInc;

    private Integer carGroupCount;

    private String projectNo;

}
