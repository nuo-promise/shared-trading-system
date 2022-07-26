package cn.suparking.customer.api.beans.cargroup;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarGroupPeriodDTO {

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
}
