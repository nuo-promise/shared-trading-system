package cn.suparking.invoice.controller;

import api.beans.InvoiceModelQueryDTO;
import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.common.api.utils.SpkCommonAssert;
import cn.suparking.invoice.dao.entity.InvoiceModelDO;
import cn.suparking.invoice.dao.vo.InvoiceModelVO;
import cn.suparking.invoice.service.InvoiceModelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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
     * 获取用户开票历史列表.
     *
     * @param invoiceModelQueryDTO {@linkplain InvoiceModelQueryDTO}
     * @return {@linkplain SpkCommonResult}
     */
    @PostMapping("invoiceModelList")
    public List<InvoiceModelVO> invoiceModelList(@RequestBody final InvoiceModelQueryDTO invoiceModelQueryDTO) {
        return invoiceModelService.invoiceModelList(invoiceModelQueryDTO);
    }

    /**
     * 开票.
     *
     * @param invoiceModelQueryDTO {@linkplain InvoiceModelQueryDTO}
     * @return {@linkplain SpkCommonResult}
     */
    @PostMapping("makeInvoiceModel")
    public SpkCommonResult makeInvoiceModel(@RequestBody final InvoiceModelQueryDTO invoiceModelQueryDTO) {
        return Optional.ofNullable(invoiceModelQueryDTO)
                .map(item -> {
                    SpkCommonAssert.notNull(item.getUserId(), "用户id不能为空");
                    SpkCommonAssert.notNull(item.getLockOrderList(), "开票订单不能为空");
                    return invoiceModelService.makeInvoiceModel(invoiceModelQueryDTO);
                }).orElseGet(() -> SpkCommonResult.error("发票订单信息不能为空"));
    }
}
