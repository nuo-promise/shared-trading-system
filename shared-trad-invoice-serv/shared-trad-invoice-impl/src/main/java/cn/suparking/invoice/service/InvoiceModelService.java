package cn.suparking.invoice.service;

import api.beans.InvoiceModelDTO;
import cn.suparking.common.api.beans.SpkCommonResult;

public interface InvoiceModelService {

    /**
     * 开票.
     *
     * @param invoiceModelDTO {@linkplain InvoiceModelDTO}
     * @return {@linkplain SpkCommonResult}
     */
    SpkCommonResult makeInvoiceModel(InvoiceModelDTO invoiceModelDTO);
}
