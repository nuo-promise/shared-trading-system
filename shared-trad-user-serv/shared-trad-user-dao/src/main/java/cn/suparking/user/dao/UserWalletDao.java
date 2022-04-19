package cn.suparking.user.dao;

import cn.suparking.user.dao.entity.UserWallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.Optional;

/**
 * user wallet Dao.
 */
public interface UserWalletDao extends JpaRepository<UserWallet, String> {

    /**
     * get user wallet by id.
     * @param id userWallet id
     * @return {@link Optional}
     */
    Optional<UserWallet> findById(String id);

    /**
     * get user wallet by uer_id.
     * @param userId user_id
     * @return {@link Optional}
     */
    Optional<UserWallet> findByUserId(String userId);

    /**
     * update user wallet amount by id.
     * @param amount {@link Long} wallet amount
     * @param dateUpdated {@link Timestamp}
     * @param id wallet id
     * @return {@link int}
     */
    @Modifying
    @Query("update UserWallet u set u.amount = :amount, u.dateUpdated = :dateUpdated where u.id = :id")
    int updateUserWalletAmountById(@Param("amount") Long amount, @Param("dateUpdated") Timestamp dateUpdated, @Param("id") String id);

    /**
     * update user wallet amount by user id.
     * @param amount {@link Long} wallet amount
     * @param dateUpdated {@link Timestamp}
     * @param userId user_id
     * @return {@link int}
     */
    @Modifying
    @Query("update UserWallet u set u.amount = :amount, u.dateUpdated = :dateUpdated where u.userId = :userId")
    int updateUserWalletAmountByUserId(@Param("amount") Long amount, @Param("dateUpdated") Timestamp dateUpdated, @Param("userId") String userId);
}
