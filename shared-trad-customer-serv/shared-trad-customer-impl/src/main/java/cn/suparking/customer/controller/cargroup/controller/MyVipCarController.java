package cn.suparking.customer.controller.cargroup.controller;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.customer.api.beans.ParkPayDTO;
import cn.suparking.customer.api.beans.discount.DiscountDTO;
import cn.suparking.customer.api.beans.order.OrderDTO;
import cn.suparking.customer.api.beans.vip.VipPayDTO;
import cn.suparking.customer.controller.cargroup.service.MyVipCarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RefreshScope
@RestController
@RequestMapping("vip-car-api")
public class MyVipCarController {

    private final MyVipCarService myVipCarService;

    public MyVipCarController(final MyVipCarService myVipCarService) {
        this.myVipCarService = myVipCarService;
    }

    /**
     * 获取当前用户所办的合约信息.
     *
     * @param sign   秘钥
     * @param userId 用户id
     * @return {@link SpkCommonResult}
     * @author ZDD
     * @date 2022/7/20 14:53:11
     */
    @GetMapping("list")
    public SpkCommonResult myVipCarList(@RequestHeader("sign") final String sign, @RequestBody final String userId) {
        return myVipCarService.myVipCarList(sign, userId);
    }

    /**
     * 获取线上所有可办合约的场库列表.
     *
     * @param sign      秘钥
     * @param projectNo 场库编号
     * @return {@link SpkCommonResult}
     * @author ZDD
     * @date 2022/7/20 14:53:11
     */
    @GetMapping("projectVipCarList")
    public SpkCommonResult projectVipCarList(@RequestHeader("sign") final String sign, @RequestParam final String projectNo) {
        return myVipCarService.projectVipCarList(sign, projectNo);
    }

    /**
     * 获取线上所有可办合约列表.
     *
     * @param sign      秘钥
     * @param projectNo 场库编号
     * @return {@link SpkCommonResult}
     * @author ZDD
     * @date 2022/7/20 14:53:11
     */
    @GetMapping("protocolVipCarList")
    public SpkCommonResult protocolVipCarList(@RequestHeader("sign") final String sign, @RequestParam final String projectNo) {
        return myVipCarService.protocolVipCarList(sign, projectNo);
    }

    /**
     * 小程序办理合约下单接口.
     * @param sign C 端 使用 库存ID 签名制作.
     * @param vipPayDTO {@link VipPayDTO}
     * @return {@link SpkCommonResult}
     */
    @PostMapping("carGroupToPay")
    public SpkCommonResult carGroupToPay(@RequestHeader("sign") final String sign, @RequestBody final VipPayDTO vipPayDTO) {
        return myVipCarService.carGroupToPay(sign, vipPayDTO);
    }

    /**
     * 订单查询接口.
     * @param sign C 端 使用 tmpOrderNo 进行 签名制作.
     * @param orderDTO {@link OrderDTO}
     * @return {@link SpkCommonResult}
     */
    @PostMapping("queryOrder")
    public SpkCommonResult queryOrder(@RequestHeader("sign") final String sign, @RequestBody final OrderDTO orderDTO) {
        return myVipCarService.queryOrder(sign, orderDTO);
    }

    /**
     * 订单关闭接口.
     * @param sign C 端 使用 tmpOrderNo 进行 签名制作.
     * @param orderDTO {@link OrderDTO}
     * @return {@link SpkCommonResult}
     */
    @PostMapping("closeOrder")
    public SpkCommonResult closeOrder(@RequestHeader("sign") final String sign, @RequestBody final OrderDTO orderDTO) {
        return myVipCarService.closeOrder(sign, orderDTO);
    }

    /**
     * 清除库存缓存.
     * @param sign C 端 使用 stockKey 进行 签名制作.
     * @param orderDTO {@link OrderDTO}
     * @return {@link SpkCommonResult}
     */
    @PostMapping("clearStockInfoCache")
    public SpkCommonResult clearStockInfoCache(@RequestHeader("sign") final String sign, @RequestBody final OrderDTO orderDTO) {
        return myVipCarService.clearStockInfoCache(sign, orderDTO);
    }
}
