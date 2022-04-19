package cn.suparking.user.dao;

import cn.suparking.user.dao.entity.MerchantWalletDO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Merchant Wallet Dao.
 */
public interface MerchantWalletDao extends JpaRepository<MerchantWalletDO, String> {

    /**
     * get merchant wallet by id.
     * @param id merchant id
     * @return {@link Optional}
     */
    Optional<MerchantWalletDO> findById(String id);

    /**
     * get merchant by user id.
     * @param userId userId
     * @return {@link Optional}
     */
    Optional<MerchantWalletDO> findByUserId(String userId);

    /**
     * get merchant by user id.
     * @param merchantId merchant id
     * @return {@link Optional}
     */
    Optional<MerchantWalletDO> findByMerchantId(String merchantId);
}
