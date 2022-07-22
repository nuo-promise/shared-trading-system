package cn.suparking.customer.controller.cargroup.service;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.customer.api.beans.order.OrderDTO;
import cn.suparking.customer.api.beans.vip.VipPayDTO;

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

    /**
     * 小程序办理合约下单接口.
     * @param sign C 端 使用 库存ID 签名制作.
     * @param vipPayDTO {@link VipPayDTO}
     * @return {@link SpkCommonResult}
     */
    SpkCommonResult carGroupToPay(String sign, VipPayDTO vipPayDTO);

    /**
     * 小程序合约订单查询.
     * @param sign C 端 使用 库存ID 签名制作.
     * @param orderDTO {@link OrderDTO}
     * @return {@link SpkCommonResult}
     */
    SpkCommonResult queryOrder(String sign, OrderDTO orderDTO);

    /**
     * 小程序合约订单关单.
     * @param sign C 端 使用 库存ID 签名制作.
     * @param orderDTO {@link OrderDTO}
     * @return {@link SpkCommonResult}
     */
    SpkCommonResult closeOrder(String sign, OrderDTO orderDTO);

    /**
     * 清除库存信息.
     * @param sign C 端 使用 库存ID 签名制作.
     * @param orderDTO {@link OrderDTO}
     * @return {@link SpkCommonResult}
     */
    SpkCommonResult clearStockInfoCache(String sign, OrderDTO orderDTO);
}
