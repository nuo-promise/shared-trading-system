package cn.suparking.user.dao;

import cn.suparking.user.dao.entity.MerchantWallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Merchant Wallet Dao.
 */
public interface MerchantWalletDao extends JpaRepository<MerchantWallet, String> {

    /**
     * get merchant wallet by id.
     * @param id merchant id
     * @return {@link Optional}
     */
    Optional<MerchantWallet> findById(String id);

    /**
     * get merchant by user id.
     * @param userId userId
     * @return {@link Optional}
     */
    Optional<MerchantWallet> findByUserId(String userId);

    /**
     * get merchant by user id.
     * @param merchantId merchant id
     * @return {@link Optional}
     */
    Optional<MerchantWallet> findByMerchantId(String merchantId);
}
