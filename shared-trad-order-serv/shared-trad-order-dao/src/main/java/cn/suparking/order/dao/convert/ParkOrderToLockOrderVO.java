package cn.suparking.order.dao.convert;

import cn.suparking.common.api.utils.DateUtils;
import cn.suparking.order.dao.entity.CarGroupOrderDO;
import cn.suparking.order.dao.entity.ParkingOrderDO;
import cn.suparking.order.dao.vo.LockOrderVO;

import java.util.LinkedList;
import java.util.List;

public class ParkOrderToLockOrderVO {

    /**
     * ParkingOrder to LockOrder.
     * @param parkingOrderDOList {@link ParkingOrderDO}
     * @return {@link List}
     */
    public static LinkedList<LockOrderVO> convertToLockOrderVO(final List<ParkingOrderDO> parkingOrderDOList) {
        LinkedList<LockOrderVO> lockOrderVOList = new LinkedList<>();
        parkingOrderDOList.forEach(item -> {
            LockOrderVO lockOrderVO = LockOrderVO.builder()
                    .projectNo(item.getProjectNo())
                    .parkName("")
                    .status(item.getStatus())
                    .address("")
                    .minutes(DateUtils.formatSeconds((long) (item.getParkingMinutes() * 60)))
                    .time(DateUtils.secondToDateTime(item.getBeginTime()) + " ~ " + DateUtils.secondToDateTime(item.getEndTime()))
                    .payTime(DateUtils.secondToDateTime(item.getPayTime()))
                    .dueAmount(item.getDueAmount())
                    .invoiceState(item.getInvoiceState())
                    .refundState(item.getRefundState())
                    .build();
            lockOrderVOList.add(lockOrderVO);
        });
        return lockOrderVOList;
    }

    /**
     * ParkingOrder to LockOrder.
     * @param carGroupOrderDOList {@link ParkingOrderDO}
     * @return {@link List}
     */
    public static LinkedList<LockOrderVO> convertToLockVipOrderVO(final List<CarGroupOrderDO> carGroupOrderDOList) {
        LinkedList<LockOrderVO> lockOrderVOList = new LinkedList<>();
        carGroupOrderDOList.forEach(item -> {
            LockOrderVO lockOrderVO = LockOrderVO.builder()
                    .projectNo(item.getProjectNo())
                    .parkName("")
                    .status(item.getOrderState())
                    .address("")
                    .time(DateUtils.secondToDateTime(item.getBeginDate()) + " ~ " + DateUtils.secondToDateTime(item.getEndDate()))
                    .payTime(DateUtils.secondToDateTime(item.getPayTime()))
                    .dueAmount(item.getDueAmount())
                    .invoiceState(item.getInvoiceState())
                    .refundState(item.getRefundState())
                    .protocolName(item.getProtocolName())
                    .build();
            lockOrderVOList.add(lockOrderVO);
        });
        return lockOrderVOList;
    }
}
