package cn.suparking.invoice.service.impl;

import api.beans.OtherOrderDTO;
import cn.suparking.invoice.service.OtherOrderService;
import cn.suparking.order.entity.OtherOrderDO;
import cn.suparking.order.mapper.OtherOrderMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class OtherOrderServiceImpl implements OtherOrderService {

    private final OtherOrderMapper otherOrderMapper;

    public OtherOrderServiceImpl(final OtherOrderMapper otherOrderMapper) {
        this.otherOrderMapper = otherOrderMapper;
    }

    /**
     * 根据订单id查询订单信息.
     *
     * @param id 订单id
     * @return OtherOrderDO {@linkplain OtherOrderDO}
     */
    @Override
    public OtherOrderDO findById(final String id) {
        return otherOrderMapper.selectById(id);
    }

    /**
     * 创建或修改订单.
     *
     * @param otherOrderDTO 订单信息
     * @return Integer
     */
    @Override
    public Integer createOrUpdate(OtherOrderDTO otherOrderDTO) {
        OtherOrderDO otherOrderDO = OtherOrderDO.buildOtherOrderDO(otherOrderDTO);
        if (StringUtils.isEmpty(otherOrderDTO.getId())) {
            return otherOrderMapper.insert(otherOrderDO);
        } else {
            return otherOrderMapper.update(otherOrderDO);
        }
    }
}
