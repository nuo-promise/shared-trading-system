package cn.suparking.customer.dao.vo.cargroup;

import cn.suparking.customer.dao.entity.CarGroupPeriod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public final class CarGroupVO {
    private Long id;

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

    private String valid;

    private String remark;

    private String operator;

    private String modifier;

    private String dateCreated;

    private String dateUpdated;

    private List<CarGroupPeriod> carGroupPeriodList;
}
