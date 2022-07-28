package cn.suparking.invoice.dao.mapper;

import cn.suparking.invoice.dao.entity.InvoiceModelDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 开票信息.
 */
@Mapper
public interface InvoiceModelMapper {
    /**
     * 新增开票信息.
     *
     * @param invoiceModelDO 开票请求信息
     * @return Integer
     */
    Integer insert(InvoiceModelDO invoiceModelDO);
}
