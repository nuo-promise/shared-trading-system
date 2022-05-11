package cn.suparking.invoice.dao.mapper;

import api.beans.InvoiceInfoDTO;
import cn.suparking.invoice.dao.entity.InvoiceInfoDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 发票抬头信息.
 */
@Mapper
public interface InvoiceInfoMapper {
    /**
     * 根据id找发票抬头信息.
     *
     * @param id primary id
     * @return {@linkplain InvoiceInfoDO}
     */
    InvoiceInfoDO selectById(String id);

    /**
     * 新增发票抬头信息.
     *
     * @param invoiceInfoDTO {@linkplain InvoiceInfoDTO}
     * @return int
     */
    int insert(InvoiceInfoDTO invoiceInfoDTO);

    /**
     * 更新发票抬头信息.
     *
     * @param invoiceInfoDTO {@linkplain InvoiceInfoDTO}
     * @return int
     */
    int update(InvoiceInfoDTO invoiceInfoDTO);
}
