package cn.suparking.order.service;

import cn.suparking.order.api.beans.CarGroupRefundOrderDTO;
import cn.suparking.order.api.beans.CarGroupRefundOrderQueryDTO;
import cn.suparking.order.dao.entity.CarGroupRefundOrderDO;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface CarGroupRefundOrderService {

    /**
     * 合约退费订单列表.
     *
     * @param carGroupRefundOrderQueryDTO 退费订单信息
     * @return Integer
     */
    PageInfo<CarGroupRefundOrderDO> list(CarGroupRefundOrderQueryDTO carGroupRefundOrderQueryDTO);

    /**
     * 获取所有合约退费订单.
     *
     * @param carGroupRefundOrderQueryDTO 退费订单信息
     * @return Integer
     */
    List<CarGroupRefundOrderDO> findAll(CarGroupRefundOrderQueryDTO carGroupRefundOrderQueryDTO);

    /**
     * 根据合约id查询合约退费信息.
     *
     * @param id 合约id
     * @return CarGroupRefundOrderDO {@linkplain CarGroupRefundOrderDO}
     */
    CarGroupRefundOrderDO findById(String id);

    /**
     * 创建或修改合约退费订单.
     *
     * @param carGroupRefundOrderDTO 退费订单信息
     * @return Integer
     */
    Integer createOrUpdate(CarGroupRefundOrderDTO carGroupRefundOrderDTO);
}
