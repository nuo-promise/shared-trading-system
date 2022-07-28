package cn.suparking.customer.controller.order.controller;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.customer.controller.order.service.impl.OrderServiceImpl;
import cn.suparking.order.api.beans.ParkingOrderQueryDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RefreshScope
@RestController
@RequestMapping("park-order-api")
public class OrderController {

    private final OrderServiceImpl parkOrderService;

    public OrderController(final OrderServiceImpl parkOrderService) {
        this.parkOrderService = parkOrderService;
    }

    /**
     * 获取用户指定时间内的订单.
     *
     * @param sign                 秘钥
     * @param parkingOrderQueryDTO {@linkplain ParkingOrderQueryDTO}
     * @return {@linkplain SpkCommonResult}
     */
    @PostMapping("/getLockOrder")
    public SpkCommonResult getLockOrder(@RequestHeader("sign") final String sign, @Valid @RequestBody final ParkingOrderQueryDTO parkingOrderQueryDTO) {
        return parkOrderService.getLockOrder(sign, parkingOrderQueryDTO);
    }
}
