package cn.suparking.order.service;

import cn.suparking.order.api.beans.CarGroupOrderDTO;
import cn.suparking.order.api.beans.CarGroupOrderQueryDTO;
import cn.suparking.order.api.beans.ParkingOrderQueryDTO;
import cn.suparking.order.dao.entity.CarGroupOrderDO;
import cn.suparking.order.dao.vo.LockOrderVO;
import com.github.pagehelper.PageInfo;

import java.util.LinkedList;

public interface CarGroupOrderService {
    /**
     * 合约订单列表.
     *
     * @param carGroupOrderQueryDTO 订单信息
     * @return Integer
     */
    PageInfo<CarGroupOrderDO> list(CarGroupOrderQueryDTO carGroupOrderQueryDTO);

    /**
     * 根据合约id查询合约信息.
     *
     * @param id 合约id
     * @return CarGroupOrderDO {@linkplain CarGroupOrderDO}
     */
    CarGroupOrderDO findById(String id);

    /**
     * 创建或修改合约订单.
     *
     * @param carGroupOrderDTO 订单信息
     * @return {@link Long}
     */
    Long createOrUpdate(CarGroupOrderDTO carGroupOrderDTO);

    /**
     * 根据订单号获取合约信息.
     *
     * @param carGroupOrderDTO {@linkplain CarGroupOrderDTO}
     * @return Integer
     */
    CarGroupOrderDO findByOrderNo(CarGroupOrderDTO carGroupOrderDTO);

    /**
     * 根据订单编号查询合约订单.
     *
     * @param parkingOrderQueryDTO {@link ParkingOrderQueryDTO}
     * @return {@linkplain CarGroupOrderDO}
     */
    LinkedList<LockOrderVO> findVipOrderByUserId(ParkingOrderQueryDTO parkingOrderQueryDTO);
}
