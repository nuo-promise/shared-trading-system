package cn.suparking.user.dao.mapper;

import cn.suparking.user.dao.entity.CarParkDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * CarPark Mapper.
 */
@Mapper
public interface CarParkMapper {

    /**
     * select CarPark by id.
     * @param id primary key
     * @return {@linkplain CarParkDO}
     */
    CarParkDO selectById(Long id);

    /**
     * insert selective user.
     *
     * @param carParkDO {@linkplain CarParkDO}
     * @return rows
     */
    int insertSelective(CarParkDO carParkDO);

    /**
     * update selective user.
     *
     * @param carParkDO {@linkplain CarParkDO}
     * @return rows
     */
    int updateSelective(CarParkDO carParkDO);
}
