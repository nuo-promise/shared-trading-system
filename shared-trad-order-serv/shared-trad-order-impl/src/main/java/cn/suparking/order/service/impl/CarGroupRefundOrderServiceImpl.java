package cn.suparking.order.service.impl;

import cn.suparking.order.api.beans.CarGroupRefundOrderDTO;
import cn.suparking.order.service.CarGroupRefundOrderService;
import cn.suparking.order.dao.entity.CarGroupRefundOrderDO;
import cn.suparking.order.dao.mapper.CarGroupRefundOrderMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class CarGroupRefundOrderServiceImpl implements CarGroupRefundOrderService {
    private final CarGroupRefundOrderMapper carGroupRefundOrderMapper;

    public CarGroupRefundOrderServiceImpl(final CarGroupRefundOrderMapper carGroupRefundOrderMapper) {
        this.carGroupRefundOrderMapper = carGroupRefundOrderMapper;
    }

    @Override
    public CarGroupRefundOrderDO findById(final String id) {
        return carGroupRefundOrderMapper.selectById(id);
    }

    @Override
    public Integer createOrUpdate(final CarGroupRefundOrderDTO carGroupRefundOrderDTO) {
        CarGroupRefundOrderDO carGroupRefundOrderDO = CarGroupRefundOrderDO.buildCarGroupRefundOrderDO(carGroupRefundOrderDTO);
        if (StringUtils.isEmpty(carGroupRefundOrderDTO.getId())) {
            return carGroupRefundOrderMapper.insert(carGroupRefundOrderDO);
        }
        return carGroupRefundOrderMapper.update(carGroupRefundOrderDO);
    }
}
