package cn.suparking.user.controller;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.common.api.utils.SpkCommonAssert;
import cn.suparking.common.api.utils.SpkCommonResultMessage;
import cn.suparking.user.api.beans.UserWalletDTO;
import cn.suparking.user.service.intf.UserWalletService;
import cn.suparking.user.vo.UserWalletVO;
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
@RequestMapping("user-wallet")
public class UserWalletController {

    private final UserWalletService userWalletService;

    public UserWalletController(final UserWalletService userWalletService) {
        this.userWalletService = userWalletService;
    }

    /**
     * create user wallet.
     * @param userWalletDTO {@linkplain UserWalletDTO}
     * @return {@linkplain SpkCommonResult}
     */
    @PostMapping("")
    public SpkCommonResult createUserWallet(@Valid @RequestBody final UserWalletDTO userWalletDTO) {
        return Optional.ofNullable(userWalletDTO)
                .map(item -> {
                    SpkCommonAssert.notBlank(item.getUserId(), SpkCommonResultMessage.PARAMETER_ERROR + ": user id is not blank");
                    SpkCommonAssert.notNull(item.getAmount(), SpkCommonResultMessage.PARAMETER_ERROR + ": amount is not null");
                    Integer createCount = userWalletService.createOrUpdate(userWalletDTO);
                    return SpkCommonResult.success(SpkCommonResultMessage.CREATE_SUCCESS, createCount);
                }).orElseGet(() -> SpkCommonResult.error(SpkCommonResultMessage.USER_WALLET_CREATE_USER_ERROR));
    }

    /**
     * detail user.
     *
     * @param id  user wallet id.
     * @return {@linkplain SpkCommonResult}
     */
    @GetMapping("/{id}")
    public SpkCommonResult detailUser(@PathVariable("id") final Long id) {
        UserWalletVO userWalletVO = userWalletService.findById(id);
        return Optional.ofNullable(userWalletVO)
                .map(item -> SpkCommonResult.success(SpkCommonResultMessage.DETAIL_SUCCESS, item))
                .orElseGet(() -> SpkCommonResult.error(SpkCommonResultMessage.USER_WALLET_QUERY_ERROR));
    }
}
