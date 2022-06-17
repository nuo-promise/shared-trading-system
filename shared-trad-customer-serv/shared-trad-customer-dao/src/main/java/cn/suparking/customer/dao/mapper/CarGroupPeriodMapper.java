package cn.suparking.customer.dao.mapper;

import cn.suparking.customer.dao.entity.CarGroupPeriod;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CarGroupPeriodMapper {

    /**
     * 合约列表.
     *
     * @param carGroupId 合约id
     * @return int
     */
    List<CarGroupPeriod> findByCarGroupId(@Param("carGroupId") Long carGroupId);

    /**
     * 有效期删除.
     *
     * @param carGroupId 合约id
     * @return int
     */
    int deleteByCarGroupId(@Param("carGroupId") Long carGroupId);

    int insert(CarGroupPeriod carGroupPeriod);
}
