package cn.suparking.order.service.impl;

import api.beans.ChargeInfoDTO;
import cn.suparking.order.service.ChargeInfoService;
import cn.suparking.order.entity.ChargeInfoDO;
import cn.suparking.order.mapper.ChargeInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class ChargeInfoServiceImpl implements ChargeInfoService {

    private final ChargeInfoMapper chargeInfoMapper;

    public ChargeInfoServiceImpl(final ChargeInfoMapper chargeInfoMapper) {
        this.chargeInfoMapper = chargeInfoMapper;
    }

    @Override
    public ChargeInfoDO findById(final String id) {
        return chargeInfoMapper.selectById(id);
    }

    @Override
    public Integer createOrUpdate(final ChargeInfoDTO chargeInfoDTO) {
        ChargeInfoDO chargeInfoDO = ChargeInfoDO.buildChargeInfoDO(chargeInfoDTO);
        if (StringUtils.isEmpty(chargeInfoDTO.getId())) {
            return chargeInfoMapper.insert(chargeInfoDO);
        }
        return chargeInfoMapper.update(chargeInfoDO);
    }
}
