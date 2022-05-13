package cn.suparking.invoice.service;

import api.beans.InvoiceDTO;
import cn.suparking.invoice.dao.entity.InvoiceDO;

import java.util.List;

public interface InvoiceService {
    /**
     * 新增 | 修改 开票记录.
     *
     * @param invoiceDTO {@linkplain InvoiceDTO}
     * @return Integer
     */
    Integer createOrUpdate(InvoiceDTO invoiceDTO);

    /**
     * 根据用户id查找开票记录.
     *
     * @param userId 用户id
     * @return InvoiceDo {@linkplain InvoiceDO}
     */
    List<InvoiceDO> findByUserId(String userId);
}
