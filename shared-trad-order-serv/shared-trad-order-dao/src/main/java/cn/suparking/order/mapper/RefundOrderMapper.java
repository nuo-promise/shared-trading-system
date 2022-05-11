package cn.suparking.order.mapper;

import api.beans.RefundOrderDTO;
import cn.suparking.order.entity.RefundOrderDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 退费订单.
 */
@Mapper
public interface RefundOrderMapper {
    /**
     * 根据id查找退费订单.
     *
     * @param id primary id
     * @return {@linkplain RefundOrderDO}
     */
    RefundOrderDO selectById(String id);

    /**
     * 新增退费订单信息.
     *
     * @param refundOrderDO {@linkplain RefundOrderDO}
     * @return int
     */
    int insert(RefundOrderDO refundOrderDO);

    /**
     * 更新退费订单信息.
     *
     * @param refundOrderDO {@linkplain RefundOrderDO}
     * @return int
     */
    int update(RefundOrderDO refundOrderDO);
}
