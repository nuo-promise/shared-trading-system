package cn.suparking.order.controller;

import cn.suparking.order.api.beans.ChargeInfoDTO;
import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.common.api.utils.SpkCommonAssert;
import cn.suparking.common.api.utils.SpkCommonResultMessage;
import cn.suparking.order.service.ChargeInfoService;
import cn.suparking.order.dao.entity.ChargeInfoDO;
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
@RequestMapping("charge-info")
public class ChargeInfoController {
    private final ChargeInfoService chargeInfoService;

    public ChargeInfoController(final ChargeInfoService chargeInfoService) {
        this.chargeInfoService = chargeInfoService;
    }

    /**
     * 根据退费订单id查询退费订单信息.
     *
     * @param id 退费订单id
     * @return {@linkplain SpkCommonResult}
     */
    @GetMapping("/{id}")
    public SpkCommonResult detailChargeInfo(@PathVariable("id") final String id) {
        ChargeInfoDO chargeInfoDO = chargeInfoService.findById(id);
        return Optional.ofNullable(chargeInfoDO)
                .map(item -> SpkCommonResult.success(SpkCommonResultMessage.DETAIL_SUCCESS, item))
                .orElseGet(() -> SpkCommonResult.error("计费信息不存在"));
    }

    /**
     * 创建或修改计费信息.
     *
     * @param chargeInfoDTO 计费详情信息
     * @return Integer
     */
    @PostMapping("")
    public SpkCommonResult createChargeInfo(@Valid @RequestBody final ChargeInfoDTO chargeInfoDTO) {
        return Optional.ofNullable(chargeInfoDTO)
                .map(item -> {
                    SpkCommonAssert.notBlank(item.getParkingOrderId(), "订单ID不能为空");
                    Integer count = chargeInfoService.createOrUpdate(item);
                    return SpkCommonResult.success(SpkCommonResultMessage.CREATE_SUCCESS, count);
                }).orElseGet(() -> SpkCommonResult.error("计费信息不存在"));
    }
}
