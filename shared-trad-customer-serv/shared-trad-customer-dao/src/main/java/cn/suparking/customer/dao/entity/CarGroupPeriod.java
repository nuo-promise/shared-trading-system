package cn.suparking.customer.dao.entity;

import cn.suparking.customer.api.beans.cargroup.CarGroupDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CarGroupPeriod extends BaseDO {

    private static final long serialVersionUID = -1477645035226205491L;

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
     * build userDO.
     *
     * @param carGroupDTO {@linkplain CarGroupDTO}
     * @return {@link CarGroup}
     */
    public static CarGroupPeriod buildCarGroup(final CarGroupDTO carGroupDTO) {
        return Optional.ofNullable(carGroupDTO).map(item -> {
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            CarGroupPeriod carGroupPeriod = CarGroupPeriod.builder()
                    .carGroupId(item.getId())
                    .beginDate(item.getBeginDate())
                    .endDate(item.getEndDate())
                    .build();
            carGroupPeriod.setId(carGroupDTO.getId());
            carGroupPeriod.setDateCreated(currentTime);
            carGroupPeriod.setDateUpdated(currentTime);
            return carGroupPeriod;
        }).orElse(null);
    }
}
