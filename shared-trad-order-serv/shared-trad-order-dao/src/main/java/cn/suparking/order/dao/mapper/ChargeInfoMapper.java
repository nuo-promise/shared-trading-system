package cn.suparking.order.dao.mapper;

import cn.suparking.order.dao.entity.ChargeInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChargeInfoMapper {

    /**
     * 根据id查找缴费信息.
     *
     * @param id primary id
     * @return {@linkplain ChargeInfoDO}
     */
    ChargeInfoDO selectById(Long id);

    /**
     * 新增计费信息.
     *
     * @param chargeInfoDo {@linkplain ChargeInfoDO}
     * @return int
     */
    int insert(ChargeInfoDO chargeInfoDo);

    /**
     * 更新计费信息.
     *
     * @param chargeInfoDo {@linkplain ChargeInfoDO}
     * @return int
     */
    int update(ChargeInfoDO chargeInfoDo);

    /**
     * 根据订单获取计费信息.
     * @param parkingOrderId {@link Long}
     * @return {@link ChargeInfoDO}
     */
    List<ChargeInfoDO> findByParkingOrderId(Long parkingOrderId);
}
