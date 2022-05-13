package cn.suparking.invoice.controller;

import api.beans.InvoiceDTO;
import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.common.api.utils.SpkCommonAssert;
import cn.suparking.common.api.utils.SpkCommonResultMessage;
import cn.suparking.invoice.dao.entity.InvoiceDO;
import cn.suparking.invoice.service.InvoiceService;
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

@Slf4j
@RefreshScope
@RestController
@RequestMapping("shared-invoice/invoice")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(final InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    /**
     * 保存.
     *
     * @param invoiceDTO {@linkplain InvoiceDTO}
     * @return {@linkplain SpkCommonResult}
     */
    @PostMapping("")
    public SpkCommonResult createSharedInvoice(@Valid @RequestBody final InvoiceDTO invoiceDTO) {
        return Optional.ofNullable(invoiceDTO)
                .map(item -> {
                    SpkCommonAssert.notBlank(item.getEmail(), "邮箱地址不能为空");
                    Integer createCount = invoiceService.createOrUpdate(invoiceDTO);
                    return SpkCommonResult.success(SpkCommonResultMessage.CREATE_SUCCESS, createCount);
                }).orElseGet(() -> SpkCommonResult.error("发票抬头信息不能为空"));
    }

    /**
     * 根据发票抬头id查询抬头信息.
     *
     * @param userId 用户id
     * @return {@linkplain SpkCommonResult}
     */
    @GetMapping("/detail/{userId}")
    public SpkCommonResult findByUserId(@PathVariable("userId") final String userId) {
        List<InvoiceDO> invoiceDOList = invoiceService.findByUserId(userId);
        return SpkCommonResult.success(SpkCommonResultMessage.DETAIL_SUCCESS, invoiceDOList);
    }
}
