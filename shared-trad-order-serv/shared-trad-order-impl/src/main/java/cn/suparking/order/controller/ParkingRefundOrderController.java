package cn.suparking.order.controller;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.common.api.utils.SpkCommonAssert;
import cn.suparking.common.api.utils.SpkCommonResultMessage;
import cn.suparking.order.api.beans.ParkingRefundOrderDTO;
import cn.suparking.order.api.beans.ParkingRefundOrderQueryDTO;
import cn.suparking.order.dao.entity.ParkingRefundOrderDO;
import cn.suparking.order.service.ParkingRefundOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@Slf4j
@RefreshScope
@RestController
@RequestMapping("parking-refund-order")
public class ParkingRefundOrderController {
    private final ParkingRefundOrderService parkingRefundOrderService;

    public ParkingRefundOrderController(final ParkingRefundOrderService parkingRefundOrderService) {
        this.parkingRefundOrderService = parkingRefundOrderService;
    }

    /**
     * 根据退费订单id查询退费订单信息.
     *
     * @param id 退费订单id
     * @return {@linkplain SpkCommonResult}
     */
    @GetMapping("/{id}")
    public SpkCommonResult detailParkingRefundOrder(@PathVariable("id") final String id) {
        ParkingRefundOrderDO parkingRefundOrderDO = parkingRefundOrderService.findById(id);
        return Optional.ofNullable(parkingRefundOrderDO)
                .map(item -> SpkCommonResult.success(SpkCommonResultMessage.DETAIL_SUCCESS, item))
                .orElseGet(() -> SpkCommonResult.error("订单信息不存在"));
    }

    /**
     * 创建或修改订单信息.
     *
     * @param parkingRefundOrderDTO 订单详情信息
     * @return Integer
     */
    @PostMapping("")
    public SpkCommonResult createParkingRefundOrder(@Valid @RequestBody final ParkingRefundOrderDTO parkingRefundOrderDTO) {
        return Optional.ofNullable(parkingRefundOrderDTO)
                .map(item -> {
                    SpkCommonAssert.notBlank(item.getOrderNo(), "订单号不能为空");
                    Integer count = parkingRefundOrderService.createOrUpdate(item);
                    return SpkCommonResult.success(SpkCommonResultMessage.CREATE_SUCCESS, count);
                }).orElseGet(() -> SpkCommonResult.error("订单信息不存在"));
    }

    /**
     * 根据原支付订单号获取数据.
     *
     * @param parkingRefundOrderQueryDTO {@link ParkingRefundOrderQueryDTO}
     * @return {@link SpkCommonResult}
     */
    @PostMapping("getParkingRefundOrderByPayOrderNO")
    public SpkCommonResult getParkingRefundOrderByPayOrderNO(@RequestBody final ParkingRefundOrderQueryDTO parkingRefundOrderQueryDTO) {
        return parkingRefundOrderService.getParkingRefundOrderByPayOrderNO(parkingRefundOrderQueryDTO);
    }
}
