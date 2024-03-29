package cn.suparking.customer.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CarGroupPeriod implements Comparable<CarGroupPeriod> {

    private static final long serialVersionUID = -1477645035226205491L;

    /**
     * id.
     */
    private Long id;

    /**
     * 合约ID.
     */
    private Long carGroupId;

    /**
     * 开始时间.
     */
    private Long beginDate;

    /**
     * 结束时间.
     */
    private Long endDate;

    /**
     * 创建时间.
     */
    private Timestamp dateCreated;

    /**
     * 修改时间.
     */
    private Timestamp dateUpdated;

    @Override
    public int compareTo(final CarGroupPeriod o) {
        return (int) (this.beginDate - o.beginDate);
    }
}
