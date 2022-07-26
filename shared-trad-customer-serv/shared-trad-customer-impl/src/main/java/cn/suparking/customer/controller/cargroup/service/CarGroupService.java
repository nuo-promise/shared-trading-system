package cn.suparking.customer.controller.cargroup.service;

import cn.suparking.customer.api.beans.cargroup.CarGroupDTO;
import cn.suparking.customer.api.beans.vip.VipPayDTO;

public interface CarGroupService {
    /**
     * 合约数据校验(新增 NEW/ 续费 RENEW).
     *
     * @param vipPayDTO {@linkplain VipPayDTO}
     * @param type      办理类型:新增 NEW/ 续费 RENEW
     * @return Boolean
     * @author ZDD
     * @date 2022/7/22 15:59:28
     */
    CarGroupDTO check(VipPayDTO vipPayDTO, String type);
}
