package cn.suparking.order.service;

import api.beans.ChargeInfoDTO;
import cn.suparking.order.entity.ChargeInfoDO;

public interface ChargeInfoService {

    /**
     * 根据查询计费信息.
     *
     * @param id 计费ID
     * @return ChargeDetailDO {@linkplain ChargeInfoDO}
     */
    ChargeInfoDO findById(String id);

    /**
     * 创建或修改计费信息.
     *
     * @param chargeInfoDTO 计费详情信息
     * @return Integer
     */
    Integer createOrUpdate(ChargeInfoDTO chargeInfoDTO);
}
