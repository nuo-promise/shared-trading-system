package cn.suparking.user.dao;

import cn.suparking.user.dao.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Merchant Dao.
 */
public interface MerchantDao extends JpaRepository<Merchant, String> {

    /**
     * get merchant by id.
     * @param id merchant id
     * @return {@link Optional}
     */
    Optional<Merchant> findById(String id);

    /**
     * get merchant by iphone.
     * @param iphone merchant iphone
     * @return {@link Optional}
     */
    Optional<Merchant> findByIphone(String iphone);
}
