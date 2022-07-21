package cn.suparking.order.service.impl;

import cn.suparking.order.api.beans.DiscountInfoDTO;
import cn.suparking.order.dao.entity.DiscountInfoDO;
import cn.suparking.order.dao.mapper.DiscountInfoMapper;
import cn.suparking.order.service.DiscountInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class DiscountInfoServiceImpl implements DiscountInfoService {

    private final DiscountInfoMapper discountInfoMapper;

    public DiscountInfoServiceImpl(final DiscountInfoMapper discountInfoMapper) {
        this.discountInfoMapper = discountInfoMapper;
    }

    @Override
    public DiscountInfoDO findById(final Long id) {
        return discountInfoMapper.selectById(id);
    }

    @Override
    public Integer createOrUpdate(final DiscountInfoDTO discountInfoDTO) {
        DiscountInfoDO discountInfoDO = DiscountInfoDO.buildDiscountInfoDO(discountInfoDTO);
        if (StringUtils.isEmpty(discountInfoDTO.getId())) {
            return discountInfoMapper.insert(discountInfoDO);
        }
        return discountInfoMapper.update(discountInfoDO);
    }
}
