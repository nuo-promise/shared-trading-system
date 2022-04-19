package cn.suparking.user.dao;

import cn.suparking.user.dao.entity.MerchantDO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Merchant Dao.
 */
public interface MerchantDao extends JpaRepository<MerchantDO, String> {

    /**
     * get merchant by id.
     * @param id merchant id
     * @return {@link Optional}
     */
    Optional<MerchantDO> findById(String id);

    /**
     * get merchant by iphone.
     * @param iphone merchant iphone
     * @return {@link Optional}
     */
    Optional<MerchantDO> findByIphone(String iphone);
}
