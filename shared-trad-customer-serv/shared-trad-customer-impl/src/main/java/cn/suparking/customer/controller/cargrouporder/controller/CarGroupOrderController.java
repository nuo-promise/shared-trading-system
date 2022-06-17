package cn.suparking.customer.controller.cargrouporder.controller;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.common.api.utils.SpkCommonAssert;
import cn.suparking.customer.api.beans.cargrouporder.CarGroupOrderDTO;
import cn.suparking.customer.controller.cargrouporder.service.CarGroupOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RefreshScope
@RestController
@RequestMapping("car-group-order-api")
public class CarGroupOrderController {

    private final CarGroupOrderService carGroupOrderService;

    public CarGroupOrderController(final CarGroupOrderService carGroupOrderService) {
        this.carGroupOrderService = carGroupOrderService;
    }

    /**
     * 新增/修改合约订单.
     *
     * @param carGroupOrderDTO 订单内容
     * @return SpkCommonResult {@linkplain SpkCommonResult}
     */
    @PostMapping("createOrUpdate")
    public SpkCommonResult createOrUpdate(@RequestBody final CarGroupOrderDTO carGroupOrderDTO) {
        return Optional.ofNullable(carGroupOrderDTO).map(item -> {
            SpkCommonAssert.notNull(carGroupOrderDTO.getOrderNo(), "订单号不能为空");
            return carGroupOrderService.createOrUpdate(carGroupOrderDTO);
        }).orElseGet(() -> SpkCommonResult.error("添加"));
    }

}
