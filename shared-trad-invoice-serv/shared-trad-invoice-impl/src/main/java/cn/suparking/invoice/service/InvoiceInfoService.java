package cn.suparking.invoice.service;

import api.beans.InvoiceInfoDTO;
import api.beans.InvoiceInfoQueryDTO;
import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.invoice.dao.entity.InvoiceInfoDO;

import java.util.List;

public interface InvoiceInfoService {
    /**
     * 新增 | 修改 发票抬头.
     *
     * @param invoiceInfoDTO 发票抬头请求信息
     * @return Integer
     */
    Integer createOrUpdate(InvoiceInfoDTO invoiceInfoDTO);

    /**
     * 删除抬头信息.
     *
     * @param invoiceInfoDTO {@linkplain InvoiceInfoDTO}
     * @return {@linkplain SpkCommonResult}
     */
    Integer remove(InvoiceInfoDTO invoiceInfoDTO);

    /**
     * 用户发票抬头列表.
     *
     * @param invoiceInfoQueryDTO {@linkplain InvoiceInfoQueryDTO}
     * @return {@linkplain SpkCommonResult}
     */
    List<InvoiceInfoDO> sharedInvoiceList(InvoiceInfoQueryDTO invoiceInfoQueryDTO);
}
