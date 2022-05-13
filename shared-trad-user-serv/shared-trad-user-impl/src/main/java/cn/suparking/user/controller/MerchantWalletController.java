package cn.suparking.user.controller;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.common.api.utils.SpkCommonAssert;
import cn.suparking.common.api.utils.SpkCommonResultMessage;
import cn.suparking.user.api.beans.MerchantWalletDTO;
import cn.suparking.user.service.intf.MerchantWalletService;
import cn.suparking.user.vo.MerchantWalletVO;
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
@RequestMapping("merchant-wallet")
public class MerchantWalletController {

    private final MerchantWalletService merchantWalletService;

    public MerchantWalletController(final MerchantWalletService merchantWalletService) {
        this.merchantWalletService = merchantWalletService;
    }

    /**
     * create merchant wallet.
     * @param merchantWalletDTO {@linkplain MerchantWalletDTO}
     * @return {@linkplain SpkCommonResult}
     */
    @PostMapping("")
    public SpkCommonResult createMerchantWallet(@Valid @RequestBody final MerchantWalletDTO merchantWalletDTO) {
        return Optional.ofNullable(merchantWalletDTO)
                .map(item -> {
                    SpkCommonAssert.notBlank(item.getUserId(), SpkCommonResultMessage.PARAMETER_ERROR + ": user id is not blank");
                    SpkCommonAssert.notNull(item.getAmount(), SpkCommonResultMessage.PARAMETER_ERROR + ": merchant wallet amount is not null");
                    Integer createCount = merchantWalletService.createOrUpdate(merchantWalletDTO);
                    return SpkCommonResult.success(SpkCommonResultMessage.CREATE_SUCCESS, createCount);
                }).orElseGet(() -> SpkCommonResult.error(SpkCommonResultMessage.MERCHANT_WALLET_CREATE_USER_ERROR));
    }

    /**
     * detail carPark.
     *
     * @param id car Park id.
     * @return {@linkplain SpkCommonResult}
     */
    @GetMapping("/{id}")
    public SpkCommonResult detailMerchantWallet(@PathVariable("id") final Long id) {
        MerchantWalletVO merchantWalletVO = merchantWalletService.findById(id);
        return Optional.ofNullable(merchantWalletVO)
                .map(item -> SpkCommonResult.success(SpkCommonResultMessage.DETAIL_SUCCESS, item))
                .orElseGet(() -> SpkCommonResult.error(SpkCommonResultMessage.MERCHANT_WALLET_QUERY_ERROR));
    }
}
