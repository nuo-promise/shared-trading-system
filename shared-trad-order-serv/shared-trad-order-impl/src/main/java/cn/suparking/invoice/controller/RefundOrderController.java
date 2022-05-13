package cn.suparking.invoice.controller;

import api.beans.OtherOrderDTO;
import api.beans.RefundOrderDTO;
import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.common.api.utils.SpkCommonAssert;
import cn.suparking.common.api.utils.SpkCommonResultMessage;
import cn.suparking.invoice.service.OtherOrderService;
import cn.suparking.invoice.service.RefundOrderService;
import cn.suparking.order.entity.OtherOrderDO;
import cn.suparking.order.entity.RefundOrderDO;
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
@RequestMapping("orderCenter/refundOrder")
public class RefundOrderController {

    private final RefundOrderService refundOrderService;

    public RefundOrderController(final RefundOrderService refundOrderService) {
        this.refundOrderService = refundOrderService;
    }

    /**
     * 根据退费订单id查询退费订单信息.
     *
     * @param id 退费订单id
     * @return {@linkplain SpkCommonResult}
     */
    @GetMapping("/detail/{id}")
    public SpkCommonResult findById(@PathVariable("id") final String id) {
        RefundOrderDO refundOrderDO = refundOrderService.findById(id);
        return Optional.ofNullable(refundOrderDO)
                .map(item -> SpkCommonResult.success(SpkCommonResultMessage.DETAIL_SUCCESS, item))
                .orElseGet(() -> SpkCommonResult.error("订单不存在"));
    }

    /**
     * 创建或修改退费订单.
     *
     * @param refundOrderDTO 退费订单信息
     * @return Integer
     */
    @PostMapping("")
    public SpkCommonResult createSharedOrder(@Valid @RequestBody final RefundOrderDTO refundOrderDTO) {
        return Optional.ofNullable(refundOrderDTO)
                .map(item -> {
                    SpkCommonAssert.notBlank(item.getOriginOrderNo(), "原始订单号不能为空");
                    SpkCommonAssert.notBlank(item.getRefundNo(), "退费订单号不能为空");
                    Integer count = refundOrderService.createOrUpdate(item);
                    return SpkCommonResult.success(SpkCommonResultMessage.CREATE_SUCCESS, count);
                }).orElseGet(() -> SpkCommonResult.error("退费订单信息不能为空"));
    }
}
