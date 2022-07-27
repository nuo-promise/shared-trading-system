package cn.suparking.invoice.service.impl;

import api.beans.InvoiceDTO;
import cn.suparking.invoice.dao.entity.InvoiceDO;
import cn.suparking.invoice.dao.entity.InvoiceSourceDO;
import cn.suparking.invoice.dao.mapper.InvoiceMapper;
import cn.suparking.invoice.service.InvoiceService;
import cn.suparking.invoice.tools.InvoiceConstant;
import cn.suparking.order.api.beans.CarGroupOrderDTO;
import cn.suparking.order.api.beans.ParkingOrderDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceMapper invoiceMapper;

    public InvoiceServiceImpl(final InvoiceMapper invoiceMapper) {
        this.invoiceMapper = invoiceMapper;
    }

    /**
     * 新增 | 修改 开票记录.
     *
     * @param invoiceDTO {@linkplain InvoiceDTO}
     * @return Integer
     */
    @Override
    public Integer createOrUpdate(final InvoiceDTO invoiceDTO) {
        if (ObjectUtils.isEmpty(invoiceDTO.getId())) {
            return invoiceMapper.insert(invoiceDTO);
        } else {
            return invoiceMapper.update(invoiceDTO);
        }
    }

    /**
     * 根据用户id查找开票记录.
     *
     * @param userId 用户id
     * @return InvoiceDo {@linkplain InvoiceDO}
     */
    @Override
    public List<InvoiceDO> findByUserId(final String userId) {
        return invoiceMapper.findByUserId(userId);
    }
}
