package cn.suparking.order.service;

import cn.suparking.order.api.beans.CarGroupOrderDTO;
import cn.suparking.order.api.beans.CarGroupOrderQueryDTO;
import cn.suparking.order.dao.entity.CarGroupOrderDO;
import com.github.pagehelper.PageInfo;

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
     * @return Integer
     */
    Integer createOrUpdate(CarGroupOrderDTO carGroupOrderDTO);
}
