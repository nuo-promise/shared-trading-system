package cn.suparking.customer.controller.invoice.service;

import api.beans.InvoiceInfoDTO;
import api.beans.InvoiceInfoQueryDTO;
import api.beans.InvoiceModelQueryDTO;
import api.beans.InvoiceSourceDTO;
import cn.suparking.common.api.beans.SpkCommonResult;

public interface InvoiceService {

    /**
     * 申请开票.
     *
     * @param sign                 秘钥
     * @param invoiceModelQueryDTO {@link InvoiceModelQueryDTO}
     * @return {@link SpkCommonResult}
     */
    SpkCommonResult makeInvoiceModel(String sign, InvoiceModelQueryDTO invoiceModelQueryDTO);

    /**
     * 获取可开票列表.
     *
     * @param sign             秘钥
     * @param invoiceSourceDTO {@link InvoiceSourceDTO}
     * @return {@link SpkCommonResult}
     */
    SpkCommonResult getInvoiceSource(String sign, InvoiceSourceDTO invoiceSourceDTO);

    /**
     * 获取开票内容.
     *
     * @param sign             秘钥
     * @param invoiceSourceDTO {@link InvoiceSourceDTO}
     * @return {@link SpkCommonResult}
     */
    SpkCommonResult getInvoiceContent(String sign, InvoiceSourceDTO invoiceSourceDTO);

    /**
     * 获取用户开票历史列表.
     *
     * @param sign                 秘钥
     * @param invoiceModelQueryDTO {@linkplain InvoiceModelQueryDTO}
     * @return {@linkplain SpkCommonResult}
     */
    SpkCommonResult invoiceModelList(String sign, InvoiceModelQueryDTO invoiceModelQueryDTO);

    /**
     * 根据开票历史记录 查询对应的开票订单.
     *
     * @param sign                 秘钥
     * @param invoiceSourceDTO {@linkplain InvoiceSourceDTO}
     * @return {@linkplain SpkCommonResult}
     */
    SpkCommonResult getInvoiceSourceByNo(String sign, InvoiceSourceDTO invoiceSourceDTO);

    /**
     * 删除常用地址.
     *
     * @param sign             秘钥
     * @param invoiceInfoQueryDTO {@linkplain InvoiceInfoQueryDTO}
     * @return {@linkplain SpkCommonResult}
     */
    SpkCommonResult deleteInvoiceInfo(String sign, InvoiceInfoQueryDTO invoiceInfoQueryDTO);
}
