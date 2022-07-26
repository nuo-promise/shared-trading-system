package cn.suparking.invoice.service.impl;

import api.beans.InvoiceInfoQueryDTO;
import api.beans.InvoiceSourceDTO;
import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.invoice.dao.entity.InvoiceSourceDO;
import cn.suparking.invoice.dao.mapper.InvoiceSourceMapper;
import cn.suparking.invoice.service.InvoiceSourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Slf4j
@Service
public class InvoiceSourceServiceImpl implements InvoiceSourceService {

    private final InvoiceSourceMapper invoiceSourceMapper;

    public InvoiceSourceServiceImpl(final InvoiceSourceMapper invoiceSourceMapper) {
        this.invoiceSourceMapper = invoiceSourceMapper;
    }

    /**
     * 获取开票订单列表.
     *
     * @param invoiceSourceDTO {@linkplain InvoiceInfoQueryDTO}
     * @return {@linkplain SpkCommonResult}
     */
    @Override
    public List<InvoiceSourceDO> invoiceSourceList(final InvoiceSourceDTO invoiceSourceDTO) {
        return invoiceSourceMapper.list(invoiceSourceDTO);
    }

    /**
     * 新增 | 修改 发票抬头.
     *
     * @param invoiceSourceDTO 发票抬头请求信息
     * @return Integer
     */
    @Override
    public Integer createOrUpdate(final InvoiceSourceDTO invoiceSourceDTO) {
        if (ObjectUtils.isEmpty(invoiceSourceDTO.getId())) {
            return invoiceSourceMapper.insert(invoiceSourceDTO);
        } else {
            return invoiceSourceMapper.update(invoiceSourceDTO);
        }
    }
}
