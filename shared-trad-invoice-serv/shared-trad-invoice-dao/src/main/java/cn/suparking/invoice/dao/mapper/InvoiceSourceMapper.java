package cn.suparking.invoice.dao.mapper;

import api.beans.InvoiceInfoQueryDTO;
import api.beans.InvoiceSourceDTO;
import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.invoice.dao.entity.InvoiceSourceDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 发票订单信息.
 */
@Mapper
public interface InvoiceSourceMapper {

    /**
     * 获取开票订单列表.
     *
     * @param invoiceSourceDTO {@linkplain InvoiceInfoQueryDTO}
     * @return {@linkplain SpkCommonResult}
     */
    List<InvoiceSourceDO> list(InvoiceSourceDTO invoiceSourceDTO);

    /**
     * 新增发票抬头.
     *
     * @param invoiceSourceDTO 发票抬头请求信息
     * @return Integer
     */
    Integer insert(InvoiceSourceDTO invoiceSourceDTO);

    /**
     * 更新发票抬头.
     *
     * @param invoiceSourceDTO 发票抬头请求信息
     * @return Integer
     */
    Integer update(InvoiceSourceDTO invoiceSourceDTO);
}
