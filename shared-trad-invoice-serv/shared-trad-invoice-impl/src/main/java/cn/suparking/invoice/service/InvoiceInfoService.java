package cn.suparking.invoice.service;

import api.beans.InvoiceInfoDTO;
import cn.suparking.invoice.dao.entity.InvoiceInfoDO;

public interface InvoiceInfoService {
    /**
     * 新增 | 修改 发票抬头.
     *
     * @param invoiceInfoDTO 发票抬头请求信息
     * @return Integer
     */
    Integer createOrUpdate(InvoiceInfoDTO invoiceInfoDTO);

    /**
     * 根据id查询发票抬头信息.
     *
     * @param id 发票抬头id
     * @return InvoiceInfoDo 发票抬头信息
     */
    InvoiceInfoDO findById(String id);
}
