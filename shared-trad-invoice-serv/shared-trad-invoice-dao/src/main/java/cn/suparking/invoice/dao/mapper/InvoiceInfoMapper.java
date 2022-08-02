package cn.suparking.invoice.dao.mapper;

import api.beans.InvoiceInfoDTO;
import api.beans.InvoiceInfoQueryDTO;
import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.invoice.dao.entity.InvoiceInfoDO;
import cn.suparking.invoice.dao.vo.InvoiceInfoVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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

    /**
     * 用户发票抬头列表.
     *
     * @param userId 用户id
     * @return {@linkplain SpkCommonResult}
     */
    List<InvoiceInfoVO> findByUserId(Long userId);

    /**
     * 删除抬头信息.
     *
     * @param invoiceInfoDTO {@linkplain InvoiceInfoDTO}
     * @return {@linkplain SpkCommonResult}
     */
    Integer delete(InvoiceInfoDTO invoiceInfoDTO);
}
