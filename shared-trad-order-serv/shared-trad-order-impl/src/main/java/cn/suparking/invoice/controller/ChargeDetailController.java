package cn.suparking.invoice.controller;

import api.beans.ChargeDetailDTO;
import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.common.api.utils.SpkCommonAssert;
import cn.suparking.common.api.utils.SpkCommonResultMessage;
import cn.suparking.invoice.service.ChargeDetailService;
import cn.suparking.order.entity.ChargeDetailDO;
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
@RequestMapping("charge-detail")
public class ChargeDetailController {

    private final ChargeDetailService chargeDetailService;

    public ChargeDetailController(final ChargeDetailService chargeDetailService) {
        this.chargeDetailService = chargeDetailService;
    }

    /**
     * 根据退费订单id查询退费订单信息.
     *
     * @param id 退费订单id
     * @return {@linkplain SpkCommonResult}
     */
    @GetMapping("/{id}")
    public SpkCommonResult detailChargeDetail(@PathVariable("id") final String id) {
        ChargeDetailDO chargeDetailDO = chargeDetailService.findById(id);
        return Optional.ofNullable(chargeDetailDO)
                .map(item -> SpkCommonResult.success(SpkCommonResultMessage.DETAIL_SUCCESS, item))
                .orElseGet(() -> SpkCommonResult.error("计费详情不存在"));
    }

    /**
     * 创建或修改计费详情.
     *
     * @param chargeDetailDTO 计费详情信息
     * @return Integer
     */
    @PostMapping("")
    public SpkCommonResult createChargeDetail(@Valid @RequestBody final ChargeDetailDTO chargeDetailDTO) {
        return Optional.ofNullable(chargeDetailDTO)
                .map(item -> {
                    SpkCommonAssert.notBlank(item.getChangeInfoId(), "计费信息ID能为空");
                    Integer count = chargeDetailService.createOrUpdate(item);
                    return SpkCommonResult.success(SpkCommonResultMessage.CREATE_SUCCESS, count);
                }).orElseGet(() -> SpkCommonResult.error("计费详情信息不能为空"));
    }
}
