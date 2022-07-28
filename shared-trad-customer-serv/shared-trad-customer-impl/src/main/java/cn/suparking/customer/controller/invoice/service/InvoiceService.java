package cn.suparking.customer.controller.invoice.service;

import api.beans.InvoiceModelDTO;
import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.customer.api.beans.invoice.InvoiceModelQueryDTO;

public interface InvoiceService {

    /**
     * 申请开票.
     *
     * @param sign            秘钥
     * @param invoiceModelQueryDTO {@link InvoiceModelQueryDTO}
     * @return {@link SpkCommonResult}
     */
    SpkCommonResult makeInvoiceModel(String sign, InvoiceModelQueryDTO invoiceModelQueryDTO);
}
