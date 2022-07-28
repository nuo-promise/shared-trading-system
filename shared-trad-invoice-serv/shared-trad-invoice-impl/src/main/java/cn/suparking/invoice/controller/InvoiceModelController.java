/**
 * Description:TODO
 *
 * @author ZDD
 * @date 2022-07-27 14:38:06
 */
package cn.suparking.invoice.controller;

import api.beans.InvoiceModelDTO;
import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.common.api.utils.SpkCommonAssert;
import cn.suparking.common.api.utils.SpkCommonResultMessage;
import cn.suparking.invoice.service.InvoiceModelService;
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
@RequestMapping("invoice-model-api")
public class InvoiceModelController {

    private final InvoiceModelService invoiceModelService;

    public InvoiceModelController(final InvoiceModelService invoiceModelService) {
        this.invoiceModelService = invoiceModelService;
    }

    /**
     * 开票.
     *
     * @param invoiceModelDTO {@linkplain InvoiceModelDTO}
     * @return {@linkplain SpkCommonResult}
     */
    @PostMapping("makeInvoiceModel")
    public SpkCommonResult makeInvoiceModel(@RequestBody final InvoiceModelDTO invoiceModelDTO) {
        return Optional.ofNullable(invoiceModelDTO)
                .map(item -> {
                    SpkCommonAssert.notNull(item.getUserId(), "用户id不能为空");
                    SpkCommonAssert.notNull(item.getMakeInvoiceData(), "开票数据不能为空");
                    return invoiceModelService.makeInvoiceModel(invoiceModelDTO);
                }).orElseGet(() -> SpkCommonResult.error("发票订单信息不能为空"));
    }
}
