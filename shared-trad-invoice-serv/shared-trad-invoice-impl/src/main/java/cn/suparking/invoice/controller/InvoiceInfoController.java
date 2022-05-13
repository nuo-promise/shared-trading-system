package cn.suparking.invoice.controller;

import api.beans.InvoiceInfoDTO;
import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.common.api.utils.SpkCommonAssert;
import cn.suparking.common.api.utils.SpkCommonResultMessage;
import cn.suparking.invoice.dao.entity.InvoiceInfoDO;
import cn.suparking.invoice.service.InvoiceInfoService;
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
@RequestMapping("shared-invoice/invoiceInfo")
public class InvoiceInfoController {

    private final InvoiceInfoService invoiceInfoService;

    public InvoiceInfoController(final InvoiceInfoService invoiceInfoService) {
        this.invoiceInfoService = invoiceInfoService;
    }

    /**
     * 保存.
     *
     * @param InvoiceInfoDTO {@linkplain InvoiceInfoDTO}
     * @return {@linkplain SpkCommonResult}
     */
    @PostMapping("")
    public SpkCommonResult createSharedInvoiceInfo(@Valid @RequestBody final InvoiceInfoDTO InvoiceInfoDTO) {
        return Optional.ofNullable(InvoiceInfoDTO)
                .map(item -> {
                    SpkCommonAssert.notBlank(item.getHeadName(), "抬头名称不能为空");
                    Integer createCount = invoiceInfoService.createOrUpdate(InvoiceInfoDTO);
                    return SpkCommonResult.success(SpkCommonResultMessage.CREATE_SUCCESS, createCount);
                }).orElseGet(() -> SpkCommonResult.error("发票抬头信息不能为空"));
    }

    /**
     * 根据发票抬头id查询抬头信息.
     *
     * @param id 发票抬头id
     * @return {@linkplain SpkCommonResult}
     */
    @GetMapping("/detail/{id}")
    public SpkCommonResult detailInvoiceInfo(@PathVariable("id") final String id) {
        InvoiceInfoDO invoiceInfoDo = invoiceInfoService.findById(id);
        return Optional.ofNullable(invoiceInfoDo)
                .map(item -> SpkCommonResult.success(SpkCommonResultMessage.DETAIL_SUCCESS, item))
                .orElseGet(() -> SpkCommonResult.error(SpkCommonResultMessage.USER_QUERY_ERROR));
    }
}
