package cn.suparking.invoice.controller;

import api.beans.OtherOrderDTO;
import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.common.api.utils.SpkCommonAssert;
import cn.suparking.common.api.utils.SpkCommonResultMessage;
import cn.suparking.invoice.service.OtherOrderService;
import cn.suparking.order.entity.OtherOrderDO;
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
 * 三方订单信息.
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("orderCenter/otherOrder")
public class OtherOrderController {

    private final OtherOrderService otherOrderService;

    public OtherOrderController(final OtherOrderService otherOrderService) {
        this.otherOrderService = otherOrderService;
    }

    /**
     * 根据三方订单id查询订单信息.
     *
     * @param id 三方订单id
     * @return {@linkplain SpkCommonResult}
     */
    @GetMapping("/detail/{id}")
    public SpkCommonResult findById(@PathVariable("id") final String id) {
        OtherOrderDO otherOrderDO = otherOrderService.findById(id);
        return Optional.ofNullable(otherOrderDO)
                .map(item -> SpkCommonResult.success(SpkCommonResultMessage.DETAIL_SUCCESS, item))
                .orElseGet(() -> SpkCommonResult.error("订单不存在"));
    }

    /**
     * 创建或修改三方订单.
     *
     * @param otherOrderDTO 三方订单信息
     * @return Integer
     */
    @PostMapping("")
    public SpkCommonResult createSharedOrder(@Valid @RequestBody final OtherOrderDTO otherOrderDTO) {
        return Optional.ofNullable(otherOrderDTO)
                .map(item -> {
                    SpkCommonAssert.notBlank(item.getOrderNo(), "订单号不能为空");
                    Integer count = otherOrderService.createOrUpdate(item);
                    return SpkCommonResult.success(SpkCommonResultMessage.CREATE_SUCCESS, count);
                }).orElseGet(() -> SpkCommonResult.error("订单信息不能为空"));
    }
}
