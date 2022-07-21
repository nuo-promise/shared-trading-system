package cn.suparking.customer.controller.park.service.impl;

import cn.suparking.common.api.utils.DateUtils;
import cn.suparking.common.api.utils.HttpUtils;
import cn.suparking.customer.api.beans.discount.DiscountUsedDTO;
import cn.suparking.customer.api.device.ControlModel;
import cn.suparking.customer.configuration.properties.AdapterDeviceProperties;
import cn.suparking.customer.configuration.properties.DiscountProperties;
import cn.suparking.customer.controller.park.service.OrderService;
import cn.suparking.customer.feign.data.DataTemplateService;
import cn.suparking.customer.feign.order.OrderTemplateService;
import cn.suparking.data.api.parkfee.Parking;
import cn.suparking.data.api.parkfee.ParkingOrder;
import cn.suparking.order.api.beans.OrderDTO;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Objects;

import static cn.suparking.customer.api.constant.ParkConstant.CTP_CONTROL_DOWN_TYPE;
import static cn.suparking.customer.api.constant.ParkConstant.INTERFACE_CTP_CONTROL_DEVICE;
import static cn.suparking.customer.api.constant.ParkConstant.INTERFACE_DISCOUNT_USED;

@Slf4j
@Service("OrderService")
public class OrderServiceImpl implements OrderService {

    @Resource
    private AdapterDeviceProperties adapterDeviceProperties;

    @Resource
    private DiscountProperties discountProperties;

    private final DataTemplateService dataTemplateService;

    private final OrderTemplateService orderTemplateService;

    public OrderServiceImpl(final DataTemplateService dataTemplateService, final OrderTemplateService orderTemplateService) {
        this.dataTemplateService = dataTemplateService;
        this.orderTemplateService = orderTemplateService;
    }

    @Override
    public Boolean saveOrder(final ParkingOrder parkingOrder, final Parking parking, final String orderNo,
                             final String payType, final String termNo, final Integer amount, final String plateForm) {

        if (Objects.isNull(parkingOrder) || Objects.isNull(parking) || StringUtils.isBlank(termNo) || amount < 0
                || StringUtils.isBlank(plateForm)) {
            log.error("订单更新所需参数不能为空");
            return false;
        }

        Boolean parkingResult = dataTemplateService.createAndUpdateParking(parking);
        if (!parkingResult) {
            log.error("更新离场数据失败");
            return false;
        }

        // 4.生成parkingOrder数据
        OrderDTO orderDTO = OrderDTO.builder()
                .parkingOrder(parkingOrder)
                .orderNo(orderNo)
                .payType(payType)
                .termNo(termNo)
                .amount(amount)
                .plateForm(plateForm)
                .payTime(DateUtils.getCurrentSecond())
                .build();
        Boolean parkingOrderResult = orderTemplateService.createAndUpdateParkingOrder(orderDTO);
        if (!parkingOrderResult) {
            log.error("更新停车订单失败");
            return false;
        }
        return true;
    }

    /**
     * 开锁操作.
     * @param deviceNo device no
     * @return boolean
     */
    @Override
    public Boolean openCtpDevice(final String deviceNo) {
        try {
            if (StringUtils.isBlank(deviceNo)) {
                log.error("开锁操作所需参数不能为空");
                return false;
            }
            ControlModel controlModel = ControlModel.builder()
                    .deviceNo(deviceNo)
                    .cmdType(CTP_CONTROL_DOWN_TYPE)
                    .build();
            JSONObject result = HttpUtils.sendPost(adapterDeviceProperties.getUrl() + INTERFACE_CTP_CONTROL_DEVICE, JSON.toJSONString(controlModel));
            if (Objects.isNull(result) || result.getInteger("code") != 200) {
                log.error("开锁操作失败");
                return false;
            }
        } catch (Exception e) {
            Arrays.stream(e.getStackTrace()).forEach(item -> log.error(item.toString()));
            return false;
        }
        return true;
    }

    @Override
    public Boolean discountUsed(final DiscountUsedDTO discountUsedDTO) {
        try {
            if (Objects.isNull(discountUsedDTO) || StringUtils.isBlank(discountUsedDTO.getOrderNo())
                    || StringUtils.isBlank(discountUsedDTO.getProjectNo())
                    || StringUtils.isBlank(discountUsedDTO.getUserId())
                    || Objects.isNull(discountUsedDTO.getDiscountInfo())) {
                log.error("订单号:{},项目编号:{},用户id:{},优惠信息:{} 核销失败,参数有误", discountUsedDTO.getOrderNo(), discountUsedDTO.getProjectNo(),
                        discountUsedDTO.getUserId(), discountUsedDTO.getDiscountInfo());
                return false;
            }

            JSONObject result = HttpUtils.sendPost(discountProperties.getUrl() + INTERFACE_DISCOUNT_USED, JSON.toJSONString(discountUsedDTO));
            if (Objects.isNull(result) || !result.getString("code").equals("00000")) {
                log.error("订单号:{},项目编号:{},用户id:{},优惠信息:{} 核销失败", discountUsedDTO.getOrderNo(), discountUsedDTO.getProjectNo(),
                        discountUsedDTO.getUserId(), discountUsedDTO.getDiscountInfo());
                return false;
            }
        } catch (Exception e) {
            Arrays.stream(e.getStackTrace()).forEach(item -> log.error(item.toString()));
            return false;
        }
        return true;
    }
}
