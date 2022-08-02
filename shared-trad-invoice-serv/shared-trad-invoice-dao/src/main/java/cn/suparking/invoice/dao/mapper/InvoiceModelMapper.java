package cn.suparking.invoice.dao.mapper;

import api.beans.InvoiceModelQueryDTO;
import cn.suparking.invoice.dao.entity.InvoiceModelDO;
import cn.suparking.invoice.dao.vo.InvoiceModelVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 开票信息.
 */
@Mapper
public interface InvoiceModelMapper {
    /**
     * 新增开票信息.
     *
     * @param invoiceModelDO {@link InvoiceModelDO}
     * @return Integer
     */
    Integer insert(InvoiceModelDO invoiceModelDO);

    /**
     * 更新发票请求流水号修改发票状态.
     *
     * @param invoiceModelDO {@link InvoiceModelDO}
     * @return Integer
     */
    Integer updateByFpqqlsh(InvoiceModelDO invoiceModelDO);

    /**
     * 获取用户开票历史列表.
     *
     * @param invoiceModelQueryDTO {@linkplain InvoiceModelQueryDTO}
     * @return {@linkplain InvoiceModelDO}
     */
    List<InvoiceModelVO> invoiceModelList(InvoiceModelQueryDTO invoiceModelQueryDTO);
}
