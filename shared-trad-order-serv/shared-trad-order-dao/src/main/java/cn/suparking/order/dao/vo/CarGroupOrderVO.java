package cn.suparking.order.dao.vo;

import cn.suparking.common.api.configuration.SnowflakeConfig;
import cn.suparking.order.api.beans.CarGroupOrderDTO;
import cn.suparking.order.dao.entity.BaseDO;
import cn.suparking.order.dao.entity.CarGroupRefundOrderDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CarGroupOrderVO extends BaseDO {

    private static final long serialVersionUID = -7899903855843591165L;

    private Long userId;

    private String orderNo;

    private Long carGroupId;

    private String carTypeId;

    private String carTypeName;

    private String protocolId;

    private String protocolName;

    private Long beginDate;

    private Long endDate;

    private Integer totalAmount;

    private Integer discountedAmount;

    private Integer dueAmount;

    private String payChannel;

    private String payType;

    private Long payTime;

    private String userName;

    private String userMobile;

    private String orderState;

    private String invoiceState;

    private String invoiceTime;

    private String refundState;

    private String termNo;

    private String projectNo;

    private String orderType;

    private String creator;

    private String modifier;

    private List<CarGroupRefundOrderDO> carGroupRefundOrderDOList;

    /**
     * build CarGroupOrderDO.
     * @param carGroupOrderDTO {@link CarGroupOrderDTO}
     * @return {@link CarGroupOrderVO}
     */
    public static CarGroupOrderVO buildCarGroupOrderDO(final CarGroupOrderDTO carGroupOrderDTO) {
        return Optional.ofNullable(carGroupOrderDTO).map(item -> {
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            CarGroupOrderVO carGroupOrderDO = CarGroupOrderVO.builder()
                    .orderNo(item.getOrderNo())
                    .carGroupId(Long.valueOf(item.getCarGroupId()))
                    .carTypeId(item.getCarTypeId())
                    .carTypeName(item.getCarTypeName())
                    .protocolId(item.getProtocolId())
                    .protocolName(item.getProtocolName())
                    .beginDate(item.getBeginTime())
                    .endDate(item.getEndTime())
                    .dueAmount(item.getDueAmount())
                    .totalAmount(item.getTotalAmount())
                    .discountedAmount(item.getDiscountedAmount())
                    .payChannel(item.getPayChannel())
                    .payType(item.getPayType())
                    .payTime(item.getPayTime())
                    .userMobile(item.getUserMobile())
                    .userName(item.getUserName())
                    .orderState(item.getOrderState())
                    .invoiceState(item.getInvoiceState())
                    .refundState(item.getRefundState())
                    .termNo(item.getTermNo())
                    .projectNo(item.getProjectNo())
                    .orderType(item.getOrderType())
                    .creator(item.getCreator())
                    .modifier(item.getModifier())
                    .build();
            if (Objects.isNull(item.getId())) {
                carGroupOrderDO.setId(SnowflakeConfig.snowflakeId());
                carGroupOrderDO.setDateCreated(currentTime);
            } else {
                carGroupOrderDO.setId(Long.valueOf(item.getId()));
                carGroupOrderDO.setDateUpdated(currentTime);
            }
            return carGroupOrderDO;
        }).orElse(null);
    }
}
