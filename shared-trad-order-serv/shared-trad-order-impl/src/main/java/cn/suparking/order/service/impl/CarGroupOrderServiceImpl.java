package cn.suparking.order.service.impl;

import cn.suparking.order.service.CarGroupOrderService;
import cn.suparking.order.api.beans.CarGroupOrderDTO;
import cn.suparking.order.dao.entity.CarGroupOrderDO;
import cn.suparking.order.dao.mapper.CarGroupOrderMapper;
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
