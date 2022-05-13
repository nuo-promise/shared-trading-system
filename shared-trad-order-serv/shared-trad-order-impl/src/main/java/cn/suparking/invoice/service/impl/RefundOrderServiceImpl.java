package cn.suparking.invoice.service.impl;

import api.beans.RefundOrderDTO;
import cn.suparking.invoice.service.RefundOrderService;
import cn.suparking.order.entity.RefundOrderDO;
import cn.suparking.order.mapper.RefundOrderMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class RefundOrderServiceImpl implements RefundOrderService {

    private final RefundOrderMapper refundOrderMapper;

    public RefundOrderServiceImpl(final RefundOrderMapper refundOrderMapper) {
        this.refundOrderMapper = refundOrderMapper;
    }

    /**
     * 根据退费订单id查询退费订单信息.
     *
     * @param id 订单id
     * @return RefundOrderDO {@linkplain RefundOrderDO}
     */
    @Override
    public RefundOrderDO findById(final String id) {
        return refundOrderMapper.selectById(id);
    }

    /**
     * 创建或修改退费订单.
     *
     * @param refundOrderDTO 退费订单信息
     * @return Integer
     */
    @Override
    public Integer createOrUpdate(RefundOrderDTO refundOrderDTO) {
        RefundOrderDO refundOrderDO = RefundOrderDO.buildRefundOrderDO(refundOrderDTO);
        if (StringUtils.isEmpty(refundOrderDTO.getId())) {
            return refundOrderMapper.insert(refundOrderDO);
        } else {
            return refundOrderMapper.update(refundOrderDO);
        }
    }
}
