package cn.suparking.invoice.controller;

import api.beans.OrderDTO;
import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.common.api.utils.SpkCommonAssert;
import cn.suparking.common.api.utils.SpkCommonResultMessage;
import cn.suparking.invoice.service.OrderService;
import cn.suparking.order.entity.OrderDO;
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

/**
 * 订单信息.
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("orderCenter/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(final OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * 根据订单id查询订单信息.
     *
     * @param id 订单id
     * @return {@linkplain SpkCommonResult}
     */
    @GetMapping("/detail/{id}")
    public SpkCommonResult findById(@PathVariable("id") final String id) {
        OrderDO orderDo = orderService.findById(id);
        return Optional.ofNullable(orderDo)
                .map(item -> SpkCommonResult.success(SpkCommonResultMessage.DETAIL_SUCCESS, item))
                .orElseGet(() -> SpkCommonResult.error("订单不存在"));
    }

    /**
     * 创建或修改订单.
     *
     * @param orderDTO 订单信息
     * @return Integer
     */
    @PostMapping("")
    public SpkCommonResult createSharedOrder(@Valid @RequestBody final OrderDTO orderDTO) {
        return Optional.ofNullable(orderDTO)
                .map(item -> {
                    SpkCommonAssert.notBlank(item.getOrderNo(), "订单号不能为空");
                    Integer count = orderService.createOrUpdate(item);
                    return SpkCommonResult.success(SpkCommonResultMessage.CREATE_SUCCESS, count);
                }).orElseGet(() -> SpkCommonResult.error("订单信息不能为空"));
    }

}
