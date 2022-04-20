package cn.suparking.user.dao.mapper;

import cn.suparking.user.dao.entity.CarLicenseDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * CarLicense Mapper.
 */
@Mapper
public interface CarLicenseMapper {

    /**
     * select CarLicense by id.
     * @param id primary key
     * @return {@linkplain CarLicenseDO}
     */
    CarLicenseDO selectById(String id);

    /**
     * insert selective user.
     *
     * @param carLicenseDO {@linkplain CarLicenseDO}
     * @return rows
     */
    int insertSelective(CarLicenseDO carLicenseDO);

    /**
     * update selective user.
     *
     * @param carLicenseDO {@linkplain CarLicenseDO}
     * @return rows
     */
    int updateSelective(CarLicenseDO carLicenseDO);
}
