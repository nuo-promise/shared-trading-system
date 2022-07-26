package cn.suparking.invoice.controller;

import api.beans.InvoiceInfoQueryDTO;
import api.beans.InvoiceSourceDTO;
import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.common.api.utils.SpkCommonAssert;
import cn.suparking.common.api.utils.SpkCommonResultMessage;
import cn.suparking.invoice.service.InvoiceSourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RefreshScope
@RestController
@RequestMapping("shared-invoice/invoiceSource")
public class InvoiceSourceController {

    private final InvoiceSourceService invoiceSourceService;

    public InvoiceSourceController(final InvoiceSourceService invoiceSourceService) {
        this.invoiceSourceService = invoiceSourceService;
    }

    /**
     * 获取开票订单列表.
     *
     * @param invoiceSourceDTO {@linkplain InvoiceInfoQueryDTO}
     * @return {@linkplain SpkCommonResult}
     */
    @PostMapping("invoiceSourceList")
    public SpkCommonResult invoiceSourceList(@RequestBody final InvoiceSourceDTO invoiceSourceDTO) {
        return Optional.ofNullable(invoiceSourceDTO)
                .map(item -> {
                    SpkCommonAssert.notBlank(String.valueOf(item.getUserId()), "userId不能为空");
                    return SpkCommonResult.success(SpkCommonResultMessage.CREATE_SUCCESS, invoiceSourceService.invoiceSourceList(invoiceSourceDTO));
                }).orElseGet(() -> SpkCommonResult.error("用户信息不能为空"));
    }

    /**
     * 新增 | 修改 发票订单.
     *
     * @param invoiceSourceDTO {@linkplain InvoiceSourceDTO}
     * @return {@linkplain SpkCommonResult}
     */
    @PostMapping("saveOrUpdate")
    public SpkCommonResult createSharedInvoiceInfo(@RequestBody final InvoiceSourceDTO invoiceSourceDTO) {
        return Optional.ofNullable(invoiceSourceDTO)
                .map(item -> {
                    SpkCommonAssert.notNull(item.getUserId(), "用户id不能为空");
                    Integer createCount = invoiceSourceService.createOrUpdate(invoiceSourceDTO);
                    return SpkCommonResult.success(SpkCommonResultMessage.CREATE_SUCCESS, createCount);
                }).orElseGet(() -> SpkCommonResult.error("发票订单信息不能为空"));
    }
}
