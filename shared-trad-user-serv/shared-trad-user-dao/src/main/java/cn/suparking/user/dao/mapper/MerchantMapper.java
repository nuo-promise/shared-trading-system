package cn.suparking.user.dao.mapper;

import cn.suparking.user.dao.entity.MerchantDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * Merchant Mapper.
 */
@Mapper
public interface MerchantMapper {

    /**
     * select merchant by id.
     * @param id primary key
     * @return {@linkplain MerchantDO}
     */
    MerchantDO selectById(String id);

    /**
     * insert selective user.
     *
     * @param merchantDO {@linkplain MerchantDO}
     * @return rows
     */
    int insertSelective(MerchantDO merchantDO);

    /**
     * update selective user.
     *
     * @param merchantDO {@linkplain MerchantDO}
     * @return rows
     */
    int updateSelective(MerchantDO merchantDO);
}
