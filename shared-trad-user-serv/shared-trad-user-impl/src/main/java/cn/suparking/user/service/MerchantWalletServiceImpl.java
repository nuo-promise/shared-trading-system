package cn.suparking.user.service;

import cn.suparking.user.api.beans.MerchantWalletDTO;
import cn.suparking.user.dao.entity.MerchantWalletDO;
import cn.suparking.user.dao.mapper.MerchantWalletMapper;
import cn.suparking.user.service.intf.MerchantWalletService;
import cn.suparking.user.vo.MerchantWalletVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MerchantWalletServiceImpl implements MerchantWalletService {

    private final MerchantWalletMapper merchantWalletMapper;

    public MerchantWalletServiceImpl(final MerchantWalletMapper merchantWalletMapper) {
        this.merchantWalletMapper = merchantWalletMapper;
    }

    @Override
    public int createOrUpdate(final MerchantWalletDTO merchantWalletDTO) {
        MerchantWalletDO merchantWalletDO = MerchantWalletDO.buildMerchantWalletDO(merchantWalletDTO);
        // create new user
        if (StringUtils.isEmpty(merchantWalletDTO.getId())) {
            return merchantWalletMapper.insertSelective(merchantWalletDO);
        }
        return merchantWalletMapper.updateSelective(merchantWalletDO);
    }

    @Override
    public MerchantWalletVO findById(final Long id) {
        return MerchantWalletVO.buildMerchantWalletVO(merchantWalletMapper.selectById(id));
    }
}
