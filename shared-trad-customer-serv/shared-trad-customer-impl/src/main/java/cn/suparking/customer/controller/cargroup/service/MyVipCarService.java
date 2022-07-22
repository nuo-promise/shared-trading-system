package cn.suparking.customer.controller.cargroup.service;

import cn.suparking.common.api.beans.SpkCommonResult;

public interface MyVipCarService {

    /**
     * 获取当前用户所办的合约信息.
     *
     * @param sign   秘钥
     * @param userId 用户id
     * @return {@link SpkCommonResult}
     * @author ZDD
     * @date 2022/7/20 14:53:11
     */
    SpkCommonResult myVipCarList(String sign, String userId);

    /**
     * 获取线上所有可办合约的场库列表.
     *
     * @param sign      秘钥
     * @param projectNo 场库编号
     * @return {@link SpkCommonResult}
     * @author ZDD
     * @date 2022/7/20 14:53:11
     */
    SpkCommonResult projectVipCarList(String sign, String projectNo);

    /**
     * 获取线上所有可办合约列表.
     *
     * @param sign      秘钥
     * @param projectNo 场库编号
     * @return {@link SpkCommonResult}
     * @author ZDD
     * @date 2022/7/20 14:53:11
     */
    SpkCommonResult protocolVipCarList(String sign, String projectNo);
}
