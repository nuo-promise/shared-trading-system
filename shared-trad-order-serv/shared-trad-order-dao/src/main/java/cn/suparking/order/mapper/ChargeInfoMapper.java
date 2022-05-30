package cn.suparking.order.mapper;

import cn.suparking.order.entity.ChargeInfoDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChargeInfoMapper {

    /**
     * 根据id查找缴费信息.
     *
     * @param id primary id
     * @return {@linkplain ChargeInfoDO}
     */
    ChargeInfoDO selectById(String id);

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
}
