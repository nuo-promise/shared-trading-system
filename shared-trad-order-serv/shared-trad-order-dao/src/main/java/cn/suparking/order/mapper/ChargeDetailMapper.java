package cn.suparking.order.mapper;

import cn.suparking.order.entity.ChargeDetailDO;
import org.apache.ibatis.annotations.Mapper;

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
}
