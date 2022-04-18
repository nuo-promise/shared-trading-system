package cn.suparking.user.dao;

import cn.suparking.user.dao.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserDao extends JpaRepository<User, String> {

    /**
     * find data by id.
     * @param id user id
     * @return {@link List}
     */
    List<User> findAllId(String id);
}
