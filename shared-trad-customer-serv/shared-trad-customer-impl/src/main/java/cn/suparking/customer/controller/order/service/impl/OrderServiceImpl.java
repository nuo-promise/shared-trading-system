package cn.suparking.customer.controller.order.service.impl;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.common.api.utils.DateUtils;
import cn.suparking.common.api.utils.SpkCommonResultMessage;
import cn.suparking.customer.configuration.properties.SharedProperties;
import cn.suparking.customer.controller.order.service.OrderService;
import cn.suparking.customer.controller.park.service.impl.ParkServiceImpl;
import cn.suparking.customer.feign.order.OrderTemplateService;
import cn.suparking.customer.tools.SignUtils;
import cn.suparking.customer.vo.park.ParkInfoVO;
import cn.suparking.order.api.beans.ParkingOrderQueryDTO;
import cn.suparking.order.dao.vo.LockOrderVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

@Service
public class OrderServiceImpl implements OrderService {

    private final ParkServiceImpl parkServiceImpl;

    private final OrderTemplateService orderTemplateService;

    @Resource
    private SharedProperties sharedProperties;

    public OrderServiceImpl(final OrderTemplateService orderTemplateService, final ParkServiceImpl parkServiceImpl) {
        this.orderTemplateService = orderTemplateService;
        this.parkServiceImpl = parkServiceImpl;
    }

    @Override
    public SpkCommonResult getLockOrder(final String sign, final ParkingOrderQueryDTO parkingOrderQueryDTO) {
        // 校验 sign
        if (!SignUtils.invoke(sign, parkingOrderQueryDTO.getUserId())) {
            return SpkCommonResult.error(SpkCommonResultMessage.SIGN_NOT_VALID);
        }
        Map<String, LinkedList<LockOrderVO>> lockOrderMap = new HashMap<String, LinkedList<LockOrderVO>>();
        Long currentSecond = DateUtils.getCurrentSecond();
        parkingOrderQueryDTO.setEndDate(new Timestamp(currentSecond * 1000));
        parkingOrderQueryDTO.setStartDate(DateUtils.getBeforeTimestampDay(currentSecond * 1000, sharedProperties.getOrderInterval()));
        Map<String, ParkInfoVO> parkInfoMap = parkServiceImpl.allLocationMap();

        LinkedList<LockOrderVO> lockOrderVOList = new LinkedList<>();

        if (parkingOrderQueryDTO.getType().equals("TEMP")) {
            lockOrderVOList = orderTemplateService.findOrderByUserId(parkingOrderQueryDTO);
        } else if (parkingOrderQueryDTO.getType().equals("VIP")) {
            lockOrderVOList = orderTemplateService.findVipOrderByUserId(parkingOrderQueryDTO);
        }

        if (Objects.nonNull(lockOrderVOList) && Objects.nonNull(parkInfoMap) && !lockOrderVOList.isEmpty()) {
            lockOrderVOList.forEach(item -> {
                ParkInfoVO parkInfoVO = parkInfoMap.get(item.getProjectNo());
                if (Objects.nonNull(parkInfoVO)) {
                    item.setParkName(parkInfoVO.getProjectName());
                    item.setAddress(parkInfoVO.getAddressSelect());
                    item.setLongitude(parkInfoVO.getLocation().getLongitude());
                    item.setLatitude(parkInfoVO.getLocation().getLatitude());
                    LinkedList<LockOrderVO> tmpLockOrderList = lockOrderMap.get(item.getProjectNo());
                    if (Objects.nonNull(tmpLockOrderList)) {
                        lockOrderMap.get(item.getProjectNo()).add(item);
                    } else {
                        tmpLockOrderList = new LinkedList<LockOrderVO>();
                        tmpLockOrderList.add(item);
                        lockOrderMap.put(item.getProjectNo(), tmpLockOrderList);
                    }
                }
            });
        }
        return SpkCommonResult.success(lockOrderMap);
    }
}