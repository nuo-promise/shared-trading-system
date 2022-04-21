package cn.suparking.user.service;

import cn.suparking.user.api.beans.MerchantDTO;
import cn.suparking.user.dao.entity.MerchantDO;
import cn.suparking.user.dao.mapper.MerchantMapper;
import cn.suparking.user.service.intf.MerchantService;
import cn.suparking.user.vo.MerchantVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MerchantServiceImpl implements MerchantService {

    private final MerchantMapper merchantMapper;

    public MerchantServiceImpl(final MerchantMapper merchantMapper) {
        this.merchantMapper = merchantMapper;
    }

    @Override
    public int createOrUpdate(final MerchantDTO merchantDTO) {
        MerchantDO merchantDO = MerchantDO.buildMerchantDO(merchantDTO);
        // create new user
        if (StringUtils.isEmpty(merchantDTO.getId())) {
            return merchantMapper.insertSelective(merchantDO);
        }
        return merchantMapper.updateSelective(merchantDO);
    }

    @Override
    public MerchantVO findById(final Long id) {
        return MerchantVO.buildMerchantVO(merchantMapper.selectById(id));
    }
}
