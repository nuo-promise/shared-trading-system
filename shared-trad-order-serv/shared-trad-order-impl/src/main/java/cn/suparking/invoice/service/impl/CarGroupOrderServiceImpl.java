package cn.suparking.invoice.service.impl;

import api.beans.CarGroupOrderDTO;
import cn.suparking.invoice.service.CarGroupOrderService;
import cn.suparking.order.entity.CarGroupOrderDO;
import cn.suparking.order.mapper.CarGroupOrderMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class CarGroupOrderServiceImpl implements CarGroupOrderService {

    private final CarGroupOrderMapper carGroupOrderMapper;

    public CarGroupOrderServiceImpl(final CarGroupOrderMapper carGroupOrderMapper) {
        this.carGroupOrderMapper = carGroupOrderMapper;
    }

    @Override
    public CarGroupOrderDO findById(final String id) {
        return carGroupOrderMapper.selectById(id);
    }

    @Override
    public Integer createOrUpdate(final CarGroupOrderDTO carGroupOrderDTO) {
        CarGroupOrderDO carGroupOrderDO = CarGroupOrderDO.buildCarGroupOrderDO(carGroupOrderDTO);
        if (StringUtils.isEmpty(carGroupOrderDTO.getId())) {
            return carGroupOrderMapper.insert(carGroupOrderDO);
        }
        return carGroupOrderMapper.update(carGroupOrderDO);
    }
}
