package cn.suparking.invoice.dao.mapper;

import api.beans.InvoiceInfoQueryDTO;
import api.beans.InvoiceModelQueryDTO;
import api.beans.InvoiceSourceDTO;
import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.invoice.dao.entity.InvoiceSourceDO;
import cn.suparking.invoice.dao.vo.InvoiceSourceVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
     * 获取开票订单列表.
     *
     * @param invoiceSourceDTO {@linkplain InvoiceInfoQueryDTO}
     * @return {@linkplain SpkCommonResult}
     */
    List<InvoiceSourceDO> listLikeOrderNo(InvoiceSourceDTO invoiceSourceDTO);

    /**
     * 新增发票抬头.
     *
     * @param invoiceSourceDO 发票抬头请求信息
     * @return Integer
     */
    Integer insert(InvoiceSourceDO invoiceSourceDO);

    /**
     * 更新发票抬头.
     *
     * @param invoiceSourceDO 发票抬头请求信息
     * @return Integer
     */
    Integer update(InvoiceSourceDO invoiceSourceDO);

    /**
     * 根据id删除发票信息.
     *
     * @param id 发票id
     * @return Integer
     */
    Integer deleteById(Long id);

    /**
     * 根据订单号查找开票订单数据.
     *
     * @param invoiceSourceDTO {@linkplain InvoiceSourceDTO}
     * @return {@linkplain SpkCommonResult}
     */
    InvoiceSourceDO findByOrderNo(InvoiceSourceDTO invoiceSourceDTO);

    /**
     * 小程序获取用户开票订单列表.
     *
     * @param invoiceSourceDTO {@linkplain InvoiceInfoQueryDTO}
     * @return {@linkplain SpkCommonResult}
     */
    List<InvoiceSourceVO> getInvoiceSource(InvoiceSourceDTO invoiceSourceDTO);

    /**
     * 根据订单号修改发票订单的状态和编号.
     *
     * @param invoiceModelQueryDTO {@linkplain InvoiceModelQueryDTO}
     * @return {@linkplain Integer}
     */
    Integer updateByCodeList(InvoiceModelQueryDTO invoiceModelQueryDTO);

    /**
     * 根据订单号修改发票订单的状态.
     *
     * @param invoiceCode 发票信息编号
     * @param state       状态
     * @param userId      用户id
     * @return {@linkplain Integer}
     */
    Integer updateByInvoiceCode(@Param("invoiceCode") String invoiceCode, @Param("state") String state, @Param("userId") Long userId);

    /**
     * 根据开票历史记录 查询对应的开票订单.
     *
     * @param invoiceCode 开票信息编号
     * @return {@linkplain InvoiceSourceDO}
     */
    List<InvoiceSourceVO> getInvoiceSourceByInvoiceCode(@Param("invoiceCode") String invoiceCode);
}
