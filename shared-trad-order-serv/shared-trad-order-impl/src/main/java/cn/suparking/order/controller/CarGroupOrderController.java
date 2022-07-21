package cn.suparking.order.controller;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.common.api.utils.SpkCommonResultMessage;
import cn.suparking.order.api.beans.CarGroupOrderDTO;
import cn.suparking.order.api.beans.CarGroupOrderQueryDTO;
import cn.suparking.order.dao.entity.CarGroupOrderDO;
import cn.suparking.order.service.CarGroupOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * 订单信息.
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/car-group-order")
public class CarGroupOrderController {

    private final CarGroupOrderService carGroupOrderService;

    public CarGroupOrderController(final CarGroupOrderService carGroupOrderService) {
        this.carGroupOrderService = carGroupOrderService;
    }

    /**
     * 合约订单列表.
     *
     * @param carGroupOrderQueryDTO {@link CarGroupOrderQueryDTO}
     * @return {@link List}
     */
    @PostMapping("list")
    public SpkCommonResult list(@Valid @RequestBody final CarGroupOrderQueryDTO carGroupOrderQueryDTO) {
        return SpkCommonResult.success(carGroupOrderService.list(carGroupOrderQueryDTO));
    }

    /**
     * 根据订单id查询订单信息.
     *
     * @param id 订单id
     * @return {@linkplain SpkCommonResult}
     */
    @GetMapping("/{id}")
    public SpkCommonResult detailCarGroupOrder(@PathVariable("id") final String id) {
        CarGroupOrderDO carGroupOrderDo = carGroupOrderService.findById(id);
        return Optional.ofNullable(carGroupOrderDo)
                .map(item -> SpkCommonResult.success(SpkCommonResultMessage.DETAIL_SUCCESS, item))
                .orElseGet(() -> SpkCommonResult.error("合约订单不存在"));
    }

    /**
     * 创建或修改合约订单.
     *
     * @param carGroupOrderDTO 合约订单信息
     * @return Integer
     */
    @PostMapping("")
    public Integer createCarGroupOrder(@Valid @RequestBody final CarGroupOrderDTO carGroupOrderDTO) {
        return carGroupOrderService.createOrUpdate(carGroupOrderDTO);
    }
}
