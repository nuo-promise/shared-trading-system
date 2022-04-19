package cn.suparking.user.dao;

import cn.suparking.user.dao.entity.CarLicense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.Optional;

/**
 * Car license Dao.
 */
public interface CarLicenseDao extends JpaRepository<CarLicense, String> {

    /**
     * get car license by id.
     * @param id car license id
     * @return {@link Optional}
     */
    Optional<CarLicense> findById(String id);

    /**
     * get car license by user id.
     * @param userId user id
     * @return {@link Optional}
     */
    Optional<CarLicense> findByUserId(String userId);

    /**
     * update car license by id.
     * @param carLicense car license
     * @param driverId car pic path
     * @param dateUpdated {@link Timestamp} update date
     * @param id car license id
     * @return {@link int}
     */
    @Modifying
    @Query("update CarLicense c set c.carLicense = :carLicense, c.driverId = :driverId, c.dateUpdated = :dateUpdated where c.id = :id")
    int updateLicenseById(@Param("carLicense") String carLicense, @Param("driverId") String driverId, @Param("dateUpdated") Timestamp dateUpdated, @Param("id") String id);

    /**
     * update car license by user id.
     * @param carLicense car license
     * @param driverId car pic path
     * @param dateUpdated {@link Timestamp} update date
     * @param userId user id
     * @return {@link int}
     */
    @Modifying
    @Query("update CarLicense c set c.carLicense = :carLicense, c.driverId = :driverId, c.dateUpdated = :dateUpdated where c.userId = :userId")
    int updateLicenseByUserId(@Param("carLicense") String carLicense, @Param("driverId") String driverId, @Param("dateUpdated") Timestamp dateUpdated, @Param("userId") String userId);

    /**
     * update type by id.
     * @param type type 0 否 1 是
     * @param dateUpdated {@link Timestamp} update date
     * @param id car license id
     * @return {@link int}
     */
    @Modifying
    @Query("update CarLicense c set c.type = :type, c.dateUpdated = :dateUpdated where c.id = :id")
    int updateTypeById(@Param("type") Integer type, @Param("dateUpdated") Timestamp dateUpdated, @Param("id") String id);

    /**
     * update type by user id.
     * @param type type 0 否 1 是
     * @param dateUpdated {@link Timestamp} update date
     * @param userId user id
     * @return {@link int}
     */
    @Modifying
    @Query("update CarLicense c set c.type = :type, c.dateUpdated = :dateUpdated where c.userId = :userId")
    int updateTypeByUserId(@Param("type") int type, @Param("dateUpdated") Timestamp dateUpdated, @Param("userId") String userId);
}
