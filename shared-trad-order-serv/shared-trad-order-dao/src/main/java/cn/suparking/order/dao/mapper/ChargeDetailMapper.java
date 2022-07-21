package cn.suparking.order.dao.mapper;

import cn.suparking.order.dao.entity.ChargeDetailDO;
import cn.suparking.order.dao.entity.ChargeInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedList;
import java.util.List;

@Mapper
public interface ChargeDetailMapper {

    /**
     * 根据id查找缴费详情.
     *
     * @param id primary id
     * @return {@linkplain ChargeDetailDO}
     */
    ChargeDetailDO selectById(String id);

    /**
     * 新增计费详情.
     *
     * @param chargeDetailDO {@linkplain ChargeDetailDO}
     * @return int
     */
    int insert(ChargeDetailDO chargeDetailDO);

    /**
     * 更新计费详情.
     *
     * @param chargeDetailDO {@linkplain ChargeDetailDO}
     * @return int
     */
    int update(ChargeDetailDO chargeDetailDO);

    /**
     * 根据订单获取计费详情信息.
     * @param chargeInfoId {@link Long}
     * @return {@link ChargeDetailDO}
     */
    LinkedList<ChargeDetailDO> findByChargeInfoId(Long chargeInfoId);
}
