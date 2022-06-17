package cn.suparking.customer.dao.mapper;

import cn.suparking.customer.api.beans.cargroup.CarGroupQueryDTO;
import cn.suparking.customer.dao.entity.CarGroup;
import cn.suparking.customer.dao.vo.cargroup.CarGroupVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CarGroupMapper {
    /**
     * 合约列表总数.
     *
     * @param carGroupQueryDTO {@link CarGroupQueryDTO}
     * @return int
     */
    long listTotal(CarGroupQueryDTO carGroupQueryDTO);

    /**
     * 合约列表.
     *
     * @param carGroupQueryDTO {@link CarGroupQueryDTO}
     * @return int
     */
    List<CarGroupVO> list(CarGroupQueryDTO carGroupQueryDTO);

    /**
     * 合约添加.
     *
     * @param carGroup {@link CarGroup}
     * @return int
     */
    int insert(CarGroup carGroup);

    /**
     * 合约修改.
     *
     * @param carGroup {@link CarGroup}
     * @return int
     */
    int update(CarGroup carGroup);

    /**
     * 合约删除.
     *
     * @param id 合约id
     * @return int
     */
    int deleteById(@Param("id") Long id);

    /**
     * 根据id查询合约信息.
     *
     * @param id 合约id
     * @return int
     */
    CarGroupVO findById(@Param("id") Long id);
}
