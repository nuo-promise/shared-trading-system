package cn.suparking.user.dao;

import cn.suparking.user.dao.entity.CarPark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Car park Dao.
 */
public interface CarParkDao extends JpaRepository<CarPark, String> {

    /**
     * get Car Park.
     * @param id car park id
     * @return {@link Optional}
     */
    Optional<CarPark> findById(String id);

    /**
     * get car park by user id.
     * @param userId user id
     * @return {@link Optional}
     */
    Optional<CarPark> findByUserId(String userId);
}
