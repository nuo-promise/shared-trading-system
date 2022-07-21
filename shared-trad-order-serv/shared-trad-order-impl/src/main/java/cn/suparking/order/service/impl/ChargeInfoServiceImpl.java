package cn.suparking.order.service.impl;

import cn.suparking.order.api.beans.ChargeInfoDTO;
import cn.suparking.order.service.ChargeInfoService;
import cn.suparking.order.dao.entity.ChargeInfoDO;
import cn.suparking.order.dao.mapper.ChargeInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class ChargeInfoServiceImpl implements ChargeInfoService {

    private final ChargeInfoMapper chargeInfoMapper;

    public ChargeInfoServiceImpl(final ChargeInfoMapper chargeInfoMapper) {
        this.chargeInfoMapper = chargeInfoMapper;
    }

    @Override
    public ChargeInfoDO findById(final Long id) {
        return chargeInfoMapper.selectById(id);
    }

    @Override
    public Long createOrUpdate(final ChargeInfoDTO chargeInfoDTO) {
        ChargeInfoDO chargeInfoDO = ChargeInfoDO.buildChargeInfoDO(chargeInfoDTO);
        if (StringUtils.isEmpty(chargeInfoDTO.getId())) {
            if (chargeInfoMapper.insert(chargeInfoDO) == 1) {
                return chargeInfoDO.getId();
            } else {
                return -1L;
            }
        } else {
            chargeInfoMapper.update(chargeInfoDO);
        }
        return chargeInfoDO.getId();
    }
}
