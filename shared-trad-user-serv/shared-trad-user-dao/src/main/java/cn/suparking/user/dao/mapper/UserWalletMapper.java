package cn.suparking.user.dao.mapper;

import cn.suparking.user.dao.entity.UserWalletDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * UserWallet Mapper.
 */
@Mapper
public interface UserWalletMapper {

    /**
     * select user wallet by id.
     * @param id primary key
     * @return {@linkplain UserWalletDO}
     */
    UserWalletDO selectById(Long id);

    /**
     * insert selective user wallet.
     *
     * @param userWalletDO {@linkplain UserWalletDO}
     * @return rows
     */
    int insertSelective(UserWalletDO userWalletDO);

    /**
     * update selective user.
     *
     * @param userWalletDO {@linkplain UserWalletDO}
     * @return rows
     */
    int updateSelective(UserWalletDO userWalletDO);
}
