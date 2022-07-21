package cn.suparking.order.controller;

import cn.suparking.order.api.beans.CarGroupOrderQueryDTO;
import cn.suparking.order.api.beans.CarGroupRefundOrderDTO;
import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.common.api.utils.SpkCommonAssert;
import cn.suparking.common.api.utils.SpkCommonResultMessage;
import cn.suparking.order.api.beans.CarGroupRefundOrderQueryDTO;
import cn.suparking.order.service.CarGroupRefundOrderService;
import cn.suparking.order.dao.entity.CarGroupRefundOrderDO;
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
 * 三方订单信息.
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("car-group-refund")
public class CarGroupRefundOrderController {

    private final CarGroupRefundOrderService carGroupRefundOrderService;

    public CarGroupRefundOrderController(final CarGroupRefundOrderService carGroupRefundOrderService) {
        this.carGroupRefundOrderService = carGroupRefundOrderService;
    }

    /**
     * 合约退费订单列表.
     *
     * @param carGroupRefundOrderQueryDTO {@link CarGroupRefundOrderQueryDTO}
     * @return {@link List}
     */
    @PostMapping("list")
    public SpkCommonResult list(@Valid @RequestBody final CarGroupRefundOrderQueryDTO carGroupRefundOrderQueryDTO) {
        return SpkCommonResult.success(carGroupRefundOrderService.list(carGroupRefundOrderQueryDTO));
    }

    /**
     * 获取所有合约退费订单.
     *
     * @param carGroupRefundOrderQueryDTO {@link CarGroupRefundOrderQueryDTO}
     * @return {@link List}
     */
    @PostMapping("findAll")
    public SpkCommonResult findAll(@Valid @RequestBody final CarGroupRefundOrderQueryDTO carGroupRefundOrderQueryDTO) {
        return SpkCommonResult.success(carGroupRefundOrderService.findAll(carGroupRefundOrderQueryDTO));
    }

    /**
     * 根据ID 查询合约退费订单.
     *
     * @param id 退费订单ID
     * @return {@linkplain SpkCommonResult}
     */
    @GetMapping("/{id}")
    public SpkCommonResult detailCarGroupRefundOrder(@PathVariable("id") final String id) {
        CarGroupRefundOrderDO carGroupRefundOrderDO = carGroupRefundOrderService.findById(id);
        return Optional.ofNullable(carGroupRefundOrderDO)
                .map(item -> SpkCommonResult.success(SpkCommonResultMessage.DETAIL_SUCCESS, item))
                .orElseGet(() -> SpkCommonResult.error("合约退费订单不存在"));
    }

    /**
     * 创建或修改合约退费订单.
     *
     * @param carGroupRefundOrderDTO 合约退费订单
     * @return Integer
     */
    @PostMapping("createCarGroupRefundOrder")
    public SpkCommonResult createCarGroupRefundOrder(@Valid @RequestBody final CarGroupRefundOrderDTO carGroupRefundOrderDTO) {
        return Optional.ofNullable(carGroupRefundOrderDTO)
                .map(item -> {
                    SpkCommonAssert.notBlank(item.getOrderNo(), "合约退费订单号不能为空");
                    Integer count = carGroupRefundOrderService.createOrUpdate(item);
                    return SpkCommonResult.success(SpkCommonResultMessage.CREATE_SUCCESS, count);
                }).orElseGet(() -> SpkCommonResult.error("合约退费订单信息不能为空"));
    }
}
