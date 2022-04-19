package cn.suparking.user.dao;

import cn.suparking.user.dao.entity.UserDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.Optional;

/**
 * User Dao.
 */
public interface UserDao extends JpaRepository<UserDO, String> {

    /**
     * find data by id.
     *
     * @param id user id
     * @return {@link Optional}
     */
    Optional<UserDO> findById(String id);

    /**
     * find user by iphone.
     * @param iphone user iphone
     * @return {@link Optional}
     */
    Optional<UserDO> findByIphone(String iphone);

    /**
     * update  user status by id.
     * @param enabled 1: active 2: no_active
     * @param dateUpdated {@link Timestamp}
     * @param id user id
     * @return {@link int}
     */
    @Modifying
    @Query("update UserDO u set u.enabled = :enabled, u.dateUpdated = :dateUpdated where u.id = :id")
    int updateUserStatusById(@Param("enabled") String enabled, @Param("dateUpdated") Timestamp dateUpdated, @Param("id") String id);

    /**
     * update user password by id.
     * @param password user password
     * @param dateUpdated {@link Timestamp}
     * @param id user id
     * @return {@link int}
     */
    @Modifying
    @Query("update UserDO u set u.password = :password, u.dateUpdated = :dateUpdated where u.id = :id")
    int updateUserPasswordById(@Param("password") String password, @Param("dateUpdated") Timestamp dateUpdated, @Param("id") String id);

    /**
     * update  user status by iphone.
     * @param enabled 1: active 2: no_active
     * @param dateUpdated {@link Timestamp}
     * @param iphone user iphone
     * @return {@link int}
     */
    @Modifying
    @Query("update UserDO u set u.enabled = :enabled, u.dateUpdated = :dateUpdated where u.iphone = :iphone")
    int updateUserStatusByIphone(@Param("enabled") String enabled, @Param("dateUpdated") Timestamp dateUpdated, @Param("iphone") String iphone);

    /**
     * update user password by iphone.
     * @param password user password
     * @param dateUpdated {@link Timestamp}
     * @param iphone user iphone
     * @return {@link int}
     */
    @Modifying
    @Query("update UserDO u set u.password = :password, u.dateUpdated = :dateUpdated where u.iphone = :iphone")
    int updateUserPasswordByIphone(@Param("password") String password, @Param("dateUpdated") Timestamp dateUpdated, @Param("iphone") String iphone);
}
