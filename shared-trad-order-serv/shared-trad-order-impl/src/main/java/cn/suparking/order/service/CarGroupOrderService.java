package cn.suparking.order.service;

import cn.suparking.order.api.beans.CarGroupOrderDTO;
import cn.suparking.order.dao.entity.CarGroupOrderDO;

public interface CarGroupOrderService {

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
     * @return Integer
     */
    Integer createOrUpdate(CarGroupOrderDTO carGroupOrderDTO);
}
