package cn.suparking.invoice.controller;

import api.beans.InvoiceInfoDTO;
import api.beans.InvoiceInfoQueryDTO;
import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.common.api.utils.SpkCommonAssert;
import cn.suparking.common.api.utils.SpkCommonResultMessage;
import cn.suparking.invoice.service.InvoiceInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RefreshScope
@RestController
@RequestMapping("shared-invoice/invoiceInfo")
public class InvoiceInfoController {

    private final InvoiceInfoService invoiceInfoService;

    public InvoiceInfoController(final InvoiceInfoService invoiceInfoService) {
        this.invoiceInfoService = invoiceInfoService;
    }

    /**
     * 用户发票抬头列表.
     *
     * @param invoiceInfoQueryDTO {@linkplain InvoiceInfoQueryDTO}
     * @return {@linkplain SpkCommonResult}
     */
    @PostMapping("sharedInvoiceList")
    public SpkCommonResult sharedInvoiceList(@RequestBody final InvoiceInfoQueryDTO invoiceInfoQueryDTO) {
        return Optional.ofNullable(invoiceInfoQueryDTO)
                .map(item -> {
                    SpkCommonAssert.notBlank(String.valueOf(item.getUserId()), "userId不能为空");
                    return SpkCommonResult.success(SpkCommonResultMessage.CREATE_SUCCESS, invoiceInfoService.sharedInvoiceList(invoiceInfoQueryDTO));
                }).orElseGet(() -> SpkCommonResult.error("用户信息不能为空"));
    }

    /**
     * 保存.
     *
     * @param invoiceInfoDTO {@linkplain InvoiceInfoDTO}
     * @return {@linkplain SpkCommonResult}
     */
    @PostMapping("saveOrUpdate")
    public SpkCommonResult createSharedInvoiceInfo(@RequestBody final InvoiceInfoDTO invoiceInfoDTO) {
        return Optional.ofNullable(invoiceInfoDTO)
                .map(item -> {
                    SpkCommonAssert.notBlank(item.getHeadName(), "抬头名称不能为空");
                    SpkCommonAssert.notBlank(item.getTaxCode(), "税号不能为空");
                    SpkCommonAssert.notNull(item.getUserId(), "用户id不能为空");
                    Integer createCount = invoiceInfoService.createOrUpdate(invoiceInfoDTO);
                    return SpkCommonResult.success(SpkCommonResultMessage.CREATE_SUCCESS, createCount);
                }).orElseGet(() -> SpkCommonResult.error("发票抬头信息不能为空"));
    }

    /**
     * 根据发票抬头id删除抬头信息.
     *
     * @param invoiceInfoDTO {@linkplain InvoiceInfoDTO}
     * @return {@linkplain SpkCommonResult}
     */
    @PostMapping("remove")
    public SpkCommonResult remove(@RequestParam final InvoiceInfoDTO invoiceInfoDTO) {
        return Optional.ofNullable(invoiceInfoDTO)
                .map(item -> {
                    SpkCommonAssert.notNull(item.getUserId(), "用户id不能为空");
                    SpkCommonAssert.notNull(item.getId(), "id不能为空");
                    Integer removeCount = invoiceInfoService.remove(invoiceInfoDTO);
                    return SpkCommonResult.success(SpkCommonResultMessage.CREATE_SUCCESS, removeCount);
                }).orElseGet(() -> SpkCommonResult.error("发票id不能为空"));
    }
}
