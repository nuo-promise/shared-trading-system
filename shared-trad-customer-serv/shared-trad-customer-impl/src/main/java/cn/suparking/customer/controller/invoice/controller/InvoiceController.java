package cn.suparking.customer.controller.invoice.controller;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.customer.api.beans.invoice.InvoiceModelQueryDTO;
import cn.suparking.customer.controller.invoice.service.InvoiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RefreshScope
@RestController
@RequestMapping("invoice-api")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(final InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    /**
     * 申请开票.
     *
     * @param sign                 秘钥
     * @param invoiceModelQueryDTO {@link InvoiceModelQueryDTO}
     * @return {@link SpkCommonResult}
     */
    @PostMapping("makeInvoiceModel")
    public SpkCommonResult makeInvoiceModel(@RequestHeader("sign") final String sign, @RequestParam final InvoiceModelQueryDTO invoiceModelQueryDTO) {
        return invoiceService.makeInvoiceModel(sign, invoiceModelQueryDTO);
    }
}
