package cn.suparking.order.mapper;

import cn.suparking.order.entity.OtherOrderDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 三方订单信息.
 */
@Mapper
public interface OtherOrderMapper {
    /**
     * 根据id查找三方订单.
     *
     * @param id primary id
     * @return {@linkplain OtherOrderDO}
     */
    OtherOrderDO selectById(String id);

    /**
     * 新增三方订单信息.
     *
     * @param otherOrderDO {@linkplain OtherOrderDO}
     * @return int
     */
    int insert(OtherOrderDO otherOrderDO);

    /**
     * 更新三方订单信息.
     *
     * @param otherOrderDO {@linkplain OtherOrderDO}
     * @return int
     */
    int update(OtherOrderDO otherOrderDO);
}
