package cn.suparking.order.dao.vo;

import cn.suparking.order.dao.entity.DiscountInfoDO;
import cn.suparking.order.dao.entity.ParkingOrderDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;

/**
 * 地锁订单真实订单之前数据.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingOrderVO extends ParkingOrderDO {

    private static final long serialVersionUID = 3896579797597231169L;

    private DiscountInfoDO discountInfoDO;

    private LinkedList<ChargeInfoVO> chargeInfos;
}
