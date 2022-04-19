package cn.suparking.user.dao;

import cn.suparking.user.dao.entity.CarParkDO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Car park Dao.
 */
public interface CarParkDao extends JpaRepository<CarParkDO, String> {

    /**
     * get Car Park.
     * @param id car park id
     * @return {@link Optional}
     */
    Optional<CarParkDO> findById(String id);

    /**
     * get car park by user id.
     * @param userId user id
     * @return {@link Optional}
     */
    Optional<CarParkDO> findByUserId(String userId);
}
