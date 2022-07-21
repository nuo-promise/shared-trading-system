package cn.suparking.order.dao.mapper;

import cn.suparking.order.dao.entity.ChargeInfoDO;
import cn.suparking.order.dao.entity.DiscountInfoDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DiscountInfoMapper {

    /**
     * 根据id查找优惠券信息.
     *
     * @param id primary id
     * @return {@linkplain DiscountInfoDO}
     */
    DiscountInfoDO selectById(Long id);

    /**
     * 新增优惠券信息.
     *
     * @param discountInfoDO {@linkplain DiscountInfoDO}
     * @return int
     */
    int insert(DiscountInfoDO discountInfoDO);

    /**
     * 更新优惠券信息.
     *
     * @param discountInfoDo {@linkplain DiscountInfoDO}
     * @return int
     */
    int update(DiscountInfoDO discountInfoDo);

    /**
     * 根据订单获取优惠信息.
     * @param parkingOrderId {@link Long}
     * @return {@link ChargeInfoDO}
     */
    DiscountInfoDO findByParkingOrderId(Long parkingOrderId);
}
