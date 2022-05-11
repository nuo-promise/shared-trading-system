package cn.suparking.invoice.dao.mapper;

import api.beans.InvoiceDTO;
import cn.suparking.invoice.dao.entity.InvoiceDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 开票记录.
 */
@Mapper
public interface InvoiceMapper {
    /**
     * 根据id找开票记录信息.
     *
     * @param id primary id
     * @return @return {@linkplain InvoiceDO}
     */
    InvoiceDO selectById(String id);

    /**
     * 新增开票记录.
     *
     * @param invoice {@linkplain InvoiceDTO}
     * @return int
     */
    int insert(InvoiceDTO invoice);

    /**
     * 更新开票记录.
     *
     * @param invoice {@linkplain InvoiceDTO}
     * @return int
     */
    int update(InvoiceDTO invoice);

    /**
     * 根据用户id查找开票记录.
     *
     * @param userId 用户id
     * @return InvoiceDo {@linkplain InvoiceDO}
     */
    List<InvoiceDO> findByUserId(String userId);
}
