package cn.suparking.invoice.service;

import api.beans.InvoiceInfoQueryDTO;
import api.beans.InvoiceSourceDTO;
import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.invoice.dao.entity.InvoiceSourceDO;

import java.util.List;

public interface InvoiceSourceService {

    /**
     * 获取开票订单列表.
     *
     * @param invoiceSourceDTO {@linkplain InvoiceInfoQueryDTO}
     * @return {@linkplain SpkCommonResult}
     */
    List<InvoiceSourceDO> invoiceSourceList(InvoiceSourceDTO invoiceSourceDTO);

    /**
     * 新增 | 修改 发票订单.
     *
     * @param invoiceSourceDTO {@linkplain InvoiceSourceDTO}
     * @return {@linkplain SpkCommonResult}
     */
    Integer createOrUpdate(InvoiceSourceDTO invoiceSourceDTO);
}
