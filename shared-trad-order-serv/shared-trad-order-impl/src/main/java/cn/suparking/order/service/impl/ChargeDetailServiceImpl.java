package cn.suparking.order.service.impl;

import cn.suparking.order.api.beans.ChargeDetailDTO;
import cn.suparking.order.service.ChargeDetailService;
import cn.suparking.order.dao.entity.ChargeDetailDO;
import cn.suparking.order.dao.mapper.ChargeDetailMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class ChargeDetailServiceImpl implements ChargeDetailService {

    private final ChargeDetailMapper chargeDetailMapper;

    public ChargeDetailServiceImpl(final ChargeDetailMapper chargeDetailMapper) {
        this.chargeDetailMapper = chargeDetailMapper;
    }

    @Override
    public ChargeDetailDO findById(final String id) {
        return chargeDetailMapper.selectById(id);
    }

    @Override
    public Integer createOrUpdate(final ChargeDetailDTO chargeDetailDTO) {
        ChargeDetailDO carGroupOrderDO = ChargeDetailDO.buildChargeDetailDO(chargeDetailDTO);
        if (StringUtils.isEmpty(chargeDetailDTO.getId())) {
            return chargeDetailMapper.insert(carGroupOrderDO);
        }
        return chargeDetailMapper.update(carGroupOrderDO);
    }
}
