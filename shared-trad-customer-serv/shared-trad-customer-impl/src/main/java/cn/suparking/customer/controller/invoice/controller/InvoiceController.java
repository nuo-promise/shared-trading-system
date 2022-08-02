package cn.suparking.customer.controller.invoice.controller;

import api.beans.InvoiceInfoDTO;
import api.beans.InvoiceInfoQueryDTO;
import api.beans.InvoiceModelQueryDTO;
import api.beans.InvoiceSourceDTO;
import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.common.api.utils.SpkCommonAssert;
import cn.suparking.common.api.utils.SpkCommonResultMessage;
import cn.suparking.customer.controller.invoice.service.InvoiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

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
    public SpkCommonResult makeInvoiceModel(@RequestHeader("sign") final String sign, @RequestBody final InvoiceModelQueryDTO invoiceModelQueryDTO) {
        return invoiceService.makeInvoiceModel(sign, invoiceModelQueryDTO);
    }

    /**
     * 获取可开票列表.
     *
     * @param sign             秘钥
     * @param invoiceSourceDTO {@link InvoiceSourceDTO}
     * @return {@link SpkCommonResult}
     */
    @PostMapping("getInvoiceSource")
    public SpkCommonResult getInvoiceSource(@RequestHeader("sign") final String sign, @RequestBody final InvoiceSourceDTO invoiceSourceDTO) {
        return invoiceService.getInvoiceSource(sign, invoiceSourceDTO);
    }

    /**
     * 获取开票内容.
     *
     * @param sign             秘钥
     * @param invoiceSourceDTO {@link InvoiceSourceDTO}
     * @return {@link SpkCommonResult}
     */
    @PostMapping("getInvoiceContent")
    public SpkCommonResult getInvoiceContent(@RequestHeader("sign") final String sign, @RequestBody final InvoiceSourceDTO invoiceSourceDTO) {
        return invoiceService.getInvoiceContent(sign, invoiceSourceDTO);
    }

    /**
     * 获取用户开票历史列表.
     *
     * @param sign                 秘钥
     * @param invoiceModelQueryDTO {@linkplain InvoiceModelQueryDTO}
     * @return {@linkplain SpkCommonResult}
     */
    @PostMapping("invoiceModelList")
    public SpkCommonResult invoiceModelList(@RequestHeader("sign") final String sign, @RequestBody final InvoiceModelQueryDTO invoiceModelQueryDTO) {
        return invoiceService.invoiceModelList(sign, invoiceModelQueryDTO);
    }

    /**
     * 根据开票历史记录 查询对应的开票订单.
     *
     * @param sign             秘钥
     * @param invoiceSourceDTO {@linkplain InvoiceSourceDTO}
     * @return {@linkplain SpkCommonResult}
     */
    @PostMapping("getInvoiceSourceByNo")
    public SpkCommonResult getInvoiceSourceByNo(@RequestHeader("sign") final String sign, @RequestBody final InvoiceSourceDTO invoiceSourceDTO) {
        return invoiceService.getInvoiceSourceByNo(sign, invoiceSourceDTO);
    }

    /**
     * 删除常用地址.
     *
     * @param sign             秘钥
     * @param invoiceInfoQueryDTO {@linkplain InvoiceInfoQueryDTO}
     * @return {@linkplain SpkCommonResult}
     */
    @PostMapping("deleteInvoiceInfo")
    public SpkCommonResult deleteInvoiceInfo(@RequestHeader("sign") final String sign, @RequestBody final InvoiceInfoQueryDTO invoiceInfoQueryDTO) {
        return Optional.ofNullable(invoiceInfoQueryDTO)
                .map(item -> {
                    SpkCommonAssert.notNull(item.getId(), "主键不能为空");
                    SpkCommonAssert.notNull(item.getUserId(), "用户不能为空");
                    return SpkCommonResult.success(SpkCommonResultMessage.CREATE_SUCCESS, invoiceService.deleteInvoiceInfo(sign, invoiceInfoQueryDTO));
                }).orElseGet(() -> SpkCommonResult.error("用户信息不能为空"));
    }
}
