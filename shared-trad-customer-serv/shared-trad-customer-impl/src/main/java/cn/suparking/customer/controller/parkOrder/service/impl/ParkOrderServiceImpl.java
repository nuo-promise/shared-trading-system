package cn.suparking.customer.controller.parkOrder.service.impl;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.common.api.utils.DateUtils;
import cn.suparking.customer.configuration.properties.SharedProperties;
import cn.suparking.customer.controller.park.service.impl.ParkServiceImpl;
import cn.suparking.customer.controller.parkOrder.service.ParkOrderService;
import cn.suparking.customer.feign.order.OrderTemplateService;
import cn.suparking.customer.vo.park.ParkInfoVO;
import cn.suparking.order.api.beans.ParkingOrderQueryDTO;
import cn.suparking.order.dao.vo.LockOrderVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

@Service
public class ParkOrderServiceImpl implements ParkOrderService {

    @Resource
    private SharedProperties sharedProperties;

    private final ParkServiceImpl parkServiceImpl;

    private final OrderTemplateService orderTemplateService;

    public ParkOrderServiceImpl(final OrderTemplateService orderTemplateService, final ParkServiceImpl parkServiceImpl) {
        this.orderTemplateService = orderTemplateService;
        this.parkServiceImpl = parkServiceImpl;
    }

    @Override
    public SpkCommonResult getLockOrder(final ParkingOrderQueryDTO parkingOrderQueryDTO) {
        Long currentSecond = DateUtils.getCurrentSecond();
        parkingOrderQueryDTO.setEndDate(new Timestamp(currentSecond * 1000));
        parkingOrderQueryDTO.setStartDate(DateUtils.getBeforeTimestampDay(currentSecond * 1000, sharedProperties.getOrderInterval()));
        Map<String, ParkInfoVO> parkInfoMap = parkServiceImpl.allLocationMap();
        LinkedList<LockOrderVO> lockOrderVOList = orderTemplateService.findOrderByUserId(parkingOrderQueryDTO);
        if (Objects.nonNull(lockOrderVOList) && Objects.nonNull(parkInfoMap) && !lockOrderVOList.isEmpty()) {
            lockOrderVOList.forEach(item -> {
                ParkInfoVO parkInfoVO = parkInfoMap.get(item.getProjectNo());
                if (Objects.nonNull(parkInfoVO)) {
                    item.setParkName(parkInfoVO.getProjectName());
                    item.setAddress(parkInfoVO.getAddressSelect());
                    item.setLongitude(parkInfoVO.getLocation().getLongitude());
                    item.setLatitude(parkInfoVO.getLocation().getLatitude());
                }
            });
        }
        return SpkCommonResult.success(lockOrderVOList);
    }
}
