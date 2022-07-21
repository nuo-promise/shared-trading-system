package cn.suparking.order.service;

import cn.suparking.order.api.beans.DiscountInfoDTO;
import cn.suparking.order.dao.entity.DiscountInfoDO;

public interface DiscountInfoService {

    /**
     * 根据优惠券使用ID 获取优惠券信息.
     *
     * @param id 计费ID
     * @return DiscountInfoDO {@linkplain DiscountInfoDO}
     */
    DiscountInfoDO findById(Long id);

    /**
     * 创建或修改使用优惠券信息.
     *
     * @param discountInfoDTO 优惠券信息
     * @return Integer
     */
    Integer createOrUpdate(DiscountInfoDTO discountInfoDTO);
}
