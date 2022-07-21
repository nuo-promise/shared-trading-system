package cn.suparking.order.service.impl;

import cn.suparking.order.api.beans.CarGroupOrderDTO;
import cn.suparking.order.api.beans.CarGroupOrderQueryDTO;
import cn.suparking.order.dao.entity.CarGroupOrderDO;
import cn.suparking.order.dao.mapper.CarGroupOrderMapper;
import cn.suparking.order.service.CarGroupOrderService;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CarGroupOrderServiceImpl implements CarGroupOrderService {

    private final CarGroupOrderMapper carGroupOrderMapper;

    public CarGroupOrderServiceImpl(final CarGroupOrderMapper carGroupOrderMapper) {
        this.carGroupOrderMapper = carGroupOrderMapper;
    }

    /**
     * 合约订单列表.
     *
     * @param carGroupOrderQueryDTO 订单信息
     * @return java.lang.String
     */
    @Override
    public PageInfo<CarGroupOrderDO> list(CarGroupOrderQueryDTO carGroupOrderQueryDTO) {
        log.info("用户 [{}] 请求获取合约订单列表,请求参数 -> {}", carGroupOrderQueryDTO.getLoginUserName(), JSONObject.toJSONString(carGroupOrderQueryDTO));
        PageHelper.startPage(carGroupOrderQueryDTO.getPageNum(), carGroupOrderQueryDTO.getPageSize());
        //求总数
        long total = carGroupOrderMapper.listTotal(carGroupOrderQueryDTO);
        List<CarGroupOrderDO> carGroupList = carGroupOrderMapper.list(carGroupOrderQueryDTO);
        PageInfo<CarGroupOrderDO> carGroupOrderDOPageInfo = new PageInfo<>(carGroupList);
        carGroupOrderDOPageInfo.setTotal(total);
        return carGroupOrderDOPageInfo;
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
