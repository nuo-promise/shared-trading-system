package cn.suparking.customer.dao.mapper;

import cn.suparking.customer.api.beans.cargroupstock.CarGroupStockQueryDTO;
import cn.suparking.customer.dao.entity.CarGroupStockDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CarGroupStockMapper {
    /**
     * 合约库存列表.
     *
     * @param carGroupStockQueryDTO {@link CarGroupStockQueryDTO}
     * @return {@linkplain CarGroupStockQueryDTO}
     */
    List<CarGroupStockDO> list(CarGroupStockQueryDTO carGroupStockQueryDTO);

    /**
     * 新增合约库存列表.
     *
     * @param carGroupStock {@link CarGroupStockDO}
     * @return int
     */
    int insert(CarGroupStockDO carGroupStock);

    /**
     * 修改合约库存列表.
     *
     * @param carGroupStock {@link CarGroupStockDO}
     * @return int
     */
    int update(CarGroupStockDO carGroupStock);

    /**
     * 根据id查找合约库存.
     *
     * @param id 主键id
     * @return {@linkplain CarGroupStockDO}
     */
    CarGroupStockDO findById(@Param("id") Long id);

    /**
     * 根据协议id查找合约库存.
     *
     * @param protocolId 协议id
     * @return {@linkplain CarGroupStockDO}
     */
    CarGroupStockDO findByProtocolId(@Param("protocolId") String protocolId);
}
