package cn.suparking.invoice.dao.mapper;

import cn.suparking.invoice.dao.entity.InvoiceDetailDO;
import cn.suparking.invoice.dao.entity.InvoiceModelDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 开票详细信息.
 */
@Mapper
public interface InvoiceDetailMapper {
    /**
     * 新增发票抬头.
     *
     * @param list {@link InvoiceDetailDO}
     * @return Integer
     */
    Integer insertBatch(List<InvoiceDetailDO> list);
}
