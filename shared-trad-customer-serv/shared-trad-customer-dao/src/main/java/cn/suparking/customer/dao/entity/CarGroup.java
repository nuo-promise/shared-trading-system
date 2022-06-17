package cn.suparking.customer.dao.entity;

import cn.suparking.common.api.configuration.SnowflakeConfig;
import cn.suparking.customer.api.beans.cargroup.CarGroupDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public final class CarGroup extends BaseDO {

    private static final long serialVersionUID = -1537990665386430042L;

    private Long userId;

    private String projectNo;

    private String carTypeId;

    private String carTypeName;

    private String protocolId;

    private String protocolType;

    private String protocolName;

    private String importNo;

    private String userMobile;

    private String address;

    private String remark;

    private String operator;

    private String modifier;

    /**
     * build userDO.
     *
     * @param carGroupDTO {@linkplain CarGroupDTO}
     * @return {@link CarGroup}
     */
    public static CarGroup buildCarGroup(final CarGroupDTO carGroupDTO) {
        return Optional.ofNullable(carGroupDTO).map(item -> {
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            CarGroup carGroup = CarGroup.builder()
                    .userId(item.getUserId())
                    .projectNo(item.getProjectNo())
                    .carTypeId(item.getCarTypeId())
                    .carTypeName(item.getCarTypeName())
                    .protocolId(item.getProtocolId())
                    .protocolType(item.getProtocolType())
                    .protocolName(item.getProtocolName())
                    .importNo(item.getImportNo())
                    .userMobile(item.getUserMobile())
                    .address(item.getAddress())
                    .remark(item.getRemark())
                    .operator(item.getOperator())
                    .modifier(item.getModifier())
                    .build();
            if (Objects.isNull(item.getId())) {
                carGroup.setId(SnowflakeConfig.snowflakeId());
                carGroup.setDateCreated(currentTime);
            } else {
                carGroup.setDateUpdated(currentTime);
            }
            return carGroup;
        }).orElse(null);
    }
}
