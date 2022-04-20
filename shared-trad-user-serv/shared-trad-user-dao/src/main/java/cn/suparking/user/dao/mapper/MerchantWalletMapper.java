package cn.suparking.user.dao.mapper;

import cn.suparking.user.dao.entity.MerchantWalletDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * MerchantWallet Mapper.
 */
@Mapper
public interface MerchantWalletMapper {

    /**
     * select merchant wallet by id.
     * @param id primary key
     * @return {@linkplain MerchantWalletDO}
     */
    MerchantWalletDO selectById(Long id);

    /**
     * insert selective user.
     *
     * @param merchantWalletDO {@linkplain MerchantWalletDO}
     * @return rows
     */
    int insertSelective(MerchantWalletDO merchantWalletDO);

    /**
     * update selective user.
     *
     * @param merchantWalletDO {@linkplain MerchantWalletDO}
     * @return rows
     */
    int updateSelective(MerchantWalletDO merchantWalletDO);
}
