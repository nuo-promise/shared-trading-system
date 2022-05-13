package cn.suparking.invoice.service.impl;

import api.beans.InvoiceInfoDTO;
import cn.suparking.common.api.exception.SpkCommonException;
import cn.suparking.invoice.dao.entity.InvoiceInfoDO;
import cn.suparking.invoice.dao.mapper.InvoiceInfoMapper;
import cn.suparking.invoice.service.InvoiceInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

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
    public Integer createOrUpdate(InvoiceInfoDTO invoiceInfoDTO) {
        if (ObjectUtils.isEmpty(invoiceInfoDTO.getId())) {
            return invoiceInfoMapper.insert(invoiceInfoDTO);
        } else {
            return invoiceInfoMapper.update(invoiceInfoDTO);
        }
    }

    /**
     * 根据id查询发票抬头信息.
     *
     * @param id 发票抬头id
     * @return InvoiceInfoDo 发票抬头信息
     */
    @Override
    public InvoiceInfoDO findById(String id) {
        return invoiceInfoMapper.selectById(id);
    }
}
