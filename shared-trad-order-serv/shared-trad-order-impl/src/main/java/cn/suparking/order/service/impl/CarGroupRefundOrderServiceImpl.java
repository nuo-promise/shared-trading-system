package cn.suparking.order.service.impl;

import cn.suparking.order.api.beans.CarGroupRefundOrderDTO;
import cn.suparking.order.api.beans.CarGroupRefundOrderQueryDTO;
import cn.suparking.order.dao.entity.CarGroupOrderDO;
import cn.suparking.order.service.CarGroupRefundOrderService;
import cn.suparking.order.dao.entity.CarGroupRefundOrderDO;
import cn.suparking.order.dao.mapper.CarGroupRefundOrderMapper;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CarGroupRefundOrderServiceImpl implements CarGroupRefundOrderService {
    private final CarGroupRefundOrderMapper carGroupRefundOrderMapper;

    @Override
    public PageInfo<CarGroupRefundOrderDO> list(CarGroupRefundOrderQueryDTO carGroupRefundOrderQueryDTO) {
        log.info("用户 [{}] 请求获取合约退费订单列表,请求参数 -> {}", carGroupRefundOrderQueryDTO.getLoginUserName(), JSONObject.toJSONString(carGroupRefundOrderQueryDTO));
        PageHelper.startPage(carGroupRefundOrderQueryDTO.getPageNum(), carGroupRefundOrderQueryDTO.getPageSize());
        //求总数
        List<CarGroupRefundOrderDO> carGroupRefundOrderDOList = carGroupRefundOrderMapper.list(carGroupRefundOrderQueryDTO);
        PageInfo<CarGroupRefundOrderDO> carGroupOrderDOPageInfo = new PageInfo<>(carGroupRefundOrderDOList);
        return carGroupOrderDOPageInfo;
    }

    @Override
    public List<CarGroupRefundOrderDO> findAll(CarGroupRefundOrderQueryDTO carGroupRefundOrderQueryDTO) {
        log.info("用户 [{}] 请求获取合约退费订单列表,请求参数 -> {}", carGroupRefundOrderQueryDTO.getLoginUserName(), JSONObject.toJSONString(carGroupRefundOrderQueryDTO));
        return carGroupRefundOrderMapper.list(carGroupRefundOrderQueryDTO);
    }

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
