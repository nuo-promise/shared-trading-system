package cn.suparking.user.controller;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.common.api.utils.SpkCommonAssert;
import cn.suparking.common.api.utils.SpkCommonResultMessage;
import cn.suparking.user.api.beans.MerchantDTO;
import cn.suparking.user.service.intf.MerchantService;
import cn.suparking.user.vo.MerchantVO;
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
@RequestMapping("merchant")
public class MerchantController {

    private final MerchantService merchantService;

    public MerchantController(final MerchantService merchantService) {
        this.merchantService = merchantService;
    }

    /**
     * create car park.
     * @param merchantDTO {@linkplain MerchantDTO}
     * @return {@linkplain SpkCommonResult}
     */
    @PostMapping("")
    public SpkCommonResult createMerchant(@Valid @RequestBody final MerchantDTO merchantDTO) {
        return Optional.ofNullable(merchantDTO)
                .map(item -> {
                    SpkCommonAssert.notBlank(item.getMerchantName(), SpkCommonResultMessage.PARAMETER_ERROR + ": merchant name is not blank");
                    SpkCommonAssert.notBlank(item.getIphone(), SpkCommonResultMessage.PARAMETER_ERROR + ": merchant iphone is not blank");
                    SpkCommonAssert.notBlank(item.getAddress(), SpkCommonResultMessage.PARAMETER_ERROR + ": address is not blank");
                    SpkCommonAssert.notBlank(item.getCardId(), SpkCommonResultMessage.PARAMETER_ERROR + ": card id is not blank");
                    SpkCommonAssert.notBlank(item.getBankCard(), SpkCommonResultMessage.PARAMETER_ERROR + ": bank card is not blank");
                    SpkCommonAssert.notNull(item.getBankCardType(), SpkCommonResultMessage.PARAMETER_ERROR + ": bank card type is not null");
                    SpkCommonAssert.notNull(item.getEnabled(), SpkCommonResultMessage.PARAMETER_ERROR + ": merchant status(enabled) id is not null");
                    Integer createCount = merchantService.createOrUpdate(merchantDTO);
                    return SpkCommonResult.success(SpkCommonResultMessage.CREATE_SUCCESS, createCount);
                }).orElseGet(() -> SpkCommonResult.error(SpkCommonResultMessage.MERCHANT_CREATE_USER_ERROR));
    }

    /**
     * detail carPark.
     *
     * @param id car Park id.
     * @return {@linkplain SpkCommonResult}
     */
    @GetMapping("/{id}")
    public SpkCommonResult detailUser(@PathVariable("id") final Long id) {
        MerchantVO merchantVO = merchantService.findById(id);
        return Optional.ofNullable(merchantVO)
                .map(item -> SpkCommonResult.success(SpkCommonResultMessage.DETAIL_SUCCESS, item))
                .orElseGet(() -> SpkCommonResult.error(SpkCommonResultMessage.MERCHANT_QUERY_ERROR));
    }
}
