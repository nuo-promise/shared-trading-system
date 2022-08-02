package cn.suparking.order.service.impl;

import cn.suparking.order.api.beans.CarGroupOrderDTO;
import cn.suparking.order.api.beans.CarGroupOrderQueryDTO;
import cn.suparking.order.api.beans.ParkingOrderQueryDTO;
import cn.suparking.order.dao.convert.ParkOrderToLockOrderVO;
import cn.suparking.order.dao.entity.CarGroupOrderDO;
import cn.suparking.order.dao.entity.CarGroupRefundOrderDO;
import cn.suparking.order.dao.mapper.CarGroupOrderMapper;
import cn.suparking.order.dao.mapper.CarGroupRefundOrderMapper;
import cn.suparking.order.dao.vo.CarGroupOrderVO;
import cn.suparking.order.dao.vo.LockOrderVO;
import cn.suparking.order.service.CarGroupOrderService;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class CarGroupOrderServiceImpl implements CarGroupOrderService {

    private final CarGroupOrderMapper carGroupOrderMapper;

    private final CarGroupRefundOrderMapper carGroupRefundOrderMapper;

    public CarGroupOrderServiceImpl(final CarGroupOrderMapper carGroupOrderMapper, final CarGroupRefundOrderMapper carGroupRefundOrderMapper) {
        this.carGroupOrderMapper = carGroupOrderMapper;
        this.carGroupRefundOrderMapper = carGroupRefundOrderMapper;
    }

    /**
     * 合约订单列表.
     *
     * @param carGroupOrderQueryDTO 订单信息
     * @return java.lang.String
     */
    @Override
    public PageInfo<CarGroupOrderVO> list(final CarGroupOrderQueryDTO carGroupOrderQueryDTO) {
        log.info("用户 [{}] 请求获取合约订单列表,请求参数 -> {}", carGroupOrderQueryDTO.getLoginUserName(), JSONObject.toJSONString(carGroupOrderQueryDTO));
        PageHelper.startPage(carGroupOrderQueryDTO.getPageNum(), carGroupOrderQueryDTO.getPageSize());
        List<CarGroupOrderVO> carGroupList = carGroupOrderMapper.list(carGroupOrderQueryDTO);
        for (CarGroupOrderVO carGroupOrderVO : carGroupList) {
            Long userId = carGroupOrderVO.getUserId();
            String orderNo = carGroupOrderVO.getOrderNo();
            List<CarGroupRefundOrderDO> carGroupRefundOrderDOList = carGroupRefundOrderMapper.selectByPayOrderNo(userId, orderNo);
            carGroupOrderVO.setCarGroupRefundOrder(carGroupRefundOrderDOList);
        }
        return new PageInfo<>(carGroupList);
    }

    @Override
    public CarGroupOrderDO findById(final String id) {
        return carGroupOrderMapper.selectById(id);
    }

    @Override
    public Long createOrUpdate(final CarGroupOrderDTO carGroupOrderDTO) {
        CarGroupOrderDO carGroupOrderDO = CarGroupOrderDO.buildCarGroupOrderDO(carGroupOrderDTO);
        if (StringUtils.isEmpty(carGroupOrderDTO.getId())) {
            if (carGroupOrderMapper.insert(carGroupOrderDO) == 1) {
                return carGroupOrderDO.getId();
            }
            return -1L;
        } else {
            carGroupOrderMapper.update(carGroupOrderDO);
        }
        return carGroupOrderDO.getId();
    }

    /**
     * 根据orderNo查找合约订单.
     *
     * @param carGroupOrderDTO {@linkplain CarGroupOrderDTO}
     * @return {@linkplain CarGroupOrderDO}
     */
    @Override
    public CarGroupOrderDO findByOrderNo(final CarGroupOrderDTO carGroupOrderDTO) {
        CarGroupOrderDO carGroupOrderDO = carGroupOrderMapper.findByOrderNo(carGroupOrderDTO.getOrderNo());
        return carGroupOrderDO;
    }

    @Override
    public LinkedList<LockOrderVO> findVipOrderByUserId(final ParkingOrderQueryDTO parkingOrderQueryDTO) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", Long.valueOf(parkingOrderQueryDTO.getUserId()));
        params.put("startDate", parkingOrderQueryDTO.getStartDate().getTime() / 1000);
        params.put("endDate", parkingOrderQueryDTO.getEndDate().getTime() / 1000);
        List<CarGroupOrderDO> carGroupOrderDOList = carGroupOrderMapper.findVipOrderByUserId(params);
        if (Objects.nonNull(carGroupOrderDOList) && !carGroupOrderDOList.isEmpty()) {
            return ParkOrderToLockOrderVO.convertToLockVipOrderVO(carGroupOrderDOList);
        }
        return null;
    }
}
