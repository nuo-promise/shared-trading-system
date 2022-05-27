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
public final class CarGroupInc extends BaseDO {

    private static final long serialVersionUID = -2078339334189153486L;

    private Long carGroupIncReportId;

    private String protocolId;

    private String protocolName;

    private Integer addCount;

    private Integer expireCount;

    private Integer inc;

    private Integer count;
}
