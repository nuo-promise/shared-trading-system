package cn.suparking.invoice.service.impl;

import api.beans.InvoiceInfoDTO;
import api.beans.InvoiceInfoQueryDTO;
import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.common.api.configuration.SnowflakeConfig;
import cn.suparking.invoice.dao.entity.InvoiceInfoDO;
import cn.suparking.invoice.dao.mapper.InvoiceInfoMapper;
import cn.suparking.invoice.dao.vo.InvoiceInfoVO;
import cn.suparking.invoice.service.InvoiceInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Slf4j
@Service
public class InvoiceInfoServiceImpl implements InvoiceInfoService {

    private final InvoiceInfoMapper invoiceInfoMapper;

    public InvoiceInfoServiceImpl(final InvoiceInfoMapper invoiceInfoMapper) {
        this.invoiceInfoMapper = invoiceInfoMapper;
    }

    /**
     * 新增 | 修改 发票抬头.
     *
     * @param invoiceInfoDTO 发票抬头请求信息
     * @return Integer
     */
    @Override
    public Integer createOrUpdate(final InvoiceInfoDTO invoiceInfoDTO) {
        if (ObjectUtils.isEmpty(invoiceInfoDTO.getId())) {
            //先判断该用户有几个常用抬头了，等于三个就不新增了
            List<InvoiceInfoVO> invoiceInfoDOList = invoiceInfoMapper.findByUserId(invoiceInfoDTO.getUserId());
            if (invoiceInfoDOList.size() == 3) {
                return 1;
            }

            invoiceInfoDTO.setId(SnowflakeConfig.snowflakeId());
            return invoiceInfoMapper.insert(invoiceInfoDTO);
        } else {
            return invoiceInfoMapper.update(invoiceInfoDTO);
        }
    }

    /**
     * 根据发票抬头id删除抬头信息.
     *
     * @param invoiceInfoDTO {@linkplain InvoiceInfoDTO}
     * @return {@linkplain SpkCommonResult}
     */
    @Override
    public Integer remove(final InvoiceInfoDTO invoiceInfoDTO) {
        return invoiceInfoMapper.delete(invoiceInfoDTO);
    }

    /**
     * 用户发票抬头列表.
     *
     * @param invoiceInfoQueryDTO {@linkplain InvoiceInfoQueryDTO}
     * @return {@linkplain SpkCommonResult}
     */
    @Override
    public List<InvoiceInfoVO> sharedInvoiceList(final InvoiceInfoQueryDTO invoiceInfoQueryDTO) {
        return invoiceInfoMapper.findByUserId(Long.valueOf(invoiceInfoQueryDTO.getUserId()));
    }
}
