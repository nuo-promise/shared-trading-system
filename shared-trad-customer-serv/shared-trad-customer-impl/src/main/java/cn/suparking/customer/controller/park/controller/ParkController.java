package cn.suparking.customer.controller.park.controller;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.common.api.utils.SpkCommonAssert;
import cn.suparking.common.api.utils.SpkCommonResultMessage;
import cn.suparking.customer.api.beans.ParkFeeQueryDTO;
import cn.suparking.customer.api.beans.ParkPayDTO;
import cn.suparking.customer.api.beans.ProjectInfoQueryDTO;
import cn.suparking.customer.api.beans.ProjectQueryDTO;
import cn.suparking.customer.api.beans.discount.DiscountDTO;
import cn.suparking.customer.api.beans.order.OrderDTO;
import cn.suparking.customer.beans.park.LocationDTO;
import cn.suparking.customer.beans.park.RegularLocationDTO;
import cn.suparking.customer.controller.park.service.ParkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@Slf4j
@RefreshScope
@RestController
@RequestMapping("park-api")
public class ParkController {

    private final ParkService parkService;

    public ParkController(final ParkService parkService) {
        this.parkService = parkService;
    }

    /**
     * 根据当前经纬度获取最近一个场库信息.
     * @param locationDTO {@link LocationDTO}
     * @return {@link SpkCommonResult}
     */
    @PostMapping("nearLocation")
    public SpkCommonResult nearByPark(@Valid @RequestBody final LocationDTO locationDTO) {
        return Optional.ofNullable(locationDTO)
               .map(item -> {
                   SpkCommonAssert.notNull(item.getLatitude(), SpkCommonResultMessage.PARAMETER_ERROR + " latitude 不能为null");
                   SpkCommonAssert.notNull(item.getLongitude(), SpkCommonResultMessage.PARAMETER_ERROR + " longitude 不能为null");
                   SpkCommonAssert.notNull(item.getNumber(), SpkCommonResultMessage.PARAMETER_ERROR + " number 不能为null");
                   SpkCommonAssert.notNull(item.getRadius(), SpkCommonResultMessage.PARAMETER_ERROR + " radius 不能为null");
                   return SpkCommonResult.success(parkService.nearByPark(item));
               }).orElseGet(() -> SpkCommonResult.error(SpkCommonResultMessage.PARAMETER_ERROR + " latitude, longitude 不能为 null"));
    }

    /**
     * 根据用户ID获取常去的场库.
     * @param regularLocationDTO {@link RegularLocationDTO}
     * @return {@link SpkCommonResult}
     */
    @PostMapping("regularLocations")
    public SpkCommonResult regularByPark(@Valid @RequestBody final RegularLocationDTO regularLocationDTO) {
        return Optional.ofNullable(regularLocationDTO)
                .map(item -> {
                    SpkCommonAssert.notBlank(item.getUserId(), SpkCommonResultMessage.PARAMETER_ERROR + " userId 不能为空");
                    return SpkCommonResult.success(parkService.regularByPark(item));
                }).orElseGet(() -> SpkCommonResult.error(SpkCommonResultMessage.PARAMETER_ERROR + " userId 不能为 null"));

    }

    /**
     * 获取所有的厂库坐标打点.
     * @return {@link SpkCommonResult}
     */
    @GetMapping("allLocation")
    public SpkCommonResult allPark() {
        return SpkCommonResult.success(parkService.allLocation());
    }

    /**
     * 如果MQ存储数据失败,那么就通过HTTP.
     * @param from Lock from MB CTP
     * @param params params
     * @return {@link SpkCommonResult}
     */
    @PostMapping("saveParkEvent")
    public SpkCommonResult saveParkEvent(@RequestHeader("from") final String from, @RequestBody final String params) {
        return SpkCommonResult.success("操作成功");
    }

    /**
     * 扫码查询费用.
     * @param sign C 端查询费用 签名
     * @param parkFeeQueryDTO {@link ParkFeeQueryDTO}
     * @return {@link SpkCommonResult}
     */
    @PostMapping("scanCodeQueryFee")
    public SpkCommonResult scanCodeQueryFee(@RequestHeader("sign") final String sign, @RequestBody final ParkFeeQueryDTO parkFeeQueryDTO) {
        return parkService.scanCodeQueryFee(sign, parkFeeQueryDTO);
    }

    /**
     * 小程序下单接口.
     * @param sign C 端 使用 tmpOrderNo 进行 签名制作.
     * @param parkPayDTO {@link ParkPayDTO}
     * @return {@link SpkCommonResult}
     */
    @PostMapping("miniToPay")
    public SpkCommonResult miniToPay(@RequestHeader("sign") final String sign, @RequestBody final ParkPayDTO parkPayDTO) {
        return parkService.miniToPay(sign, parkPayDTO);
    }

    /**
     * 订单查询接口.
     * @param sign C 端 使用 tmpOrderNo 进行 签名制作.
     * @param orderDTO {@link OrderDTO}
     * @return {@link SpkCommonResult}
     */
    @PostMapping("queryOrder")
    public SpkCommonResult queryOrder(@RequestHeader("sign") final String sign, @RequestBody final OrderDTO orderDTO) {
        return parkService.queryOrder(sign, orderDTO);
    }


    /**
     * 订单关闭接口.
     * @param sign C 端 使用 tmpOrderNo 进行 签名制作.
     * @param orderDTO {@link OrderDTO}
     * @return {@link SpkCommonResult}
     */
    @PostMapping("closeOrder")
    public SpkCommonResult closeOrder(@RequestHeader("sign") final String sign, @RequestBody final OrderDTO orderDTO) {
        return parkService.closeOrder(sign, orderDTO);
    }

    /**
     * 清除费用记录缓存.
     * @param sign C 端 使用 tmpOrderNo 进行 签名制作.
     * @param parkPayDTO {@link ParkPayDTO}
     * @return {@link SpkCommonResult}
     */
    @PostMapping("clearParkCache")
    public SpkCommonResult clearParkCache(@RequestHeader("sign") final String sign, @RequestBody final ParkPayDTO parkPayDTO) {
        return parkService.clearParkCache(sign, parkPayDTO);
    }

    /**
     * 清除优惠券缓存.
     * @param sign C 端 使用 tmpOrderNo 进行 签名制作.
     * @param discountDTO {@link DiscountDTO}
     * @return {@link SpkCommonResult}
     */
    @PostMapping("clearDiscountCache")
    public SpkCommonResult clearDiscountCache(@RequestHeader("sign") final String sign, @RequestBody final DiscountDTO discountDTO) {
        return parkService.clearDiscountCache(sign, discountDTO);
    }

    /**
     * 根据优惠券编号查询优惠券信息.
     * @param sign C 端 使用 tmpOrderNo 进行 签名制作.
     * @param discountDTO {@link DiscountDTO}
     * @return {@link SpkCommonResult}
     */
    @PostMapping("getDiscountByScan")
    public SpkCommonResult discountInfoByScanCode(@RequestHeader("sign") final String sign, @RequestBody final DiscountDTO discountDTO) {
        return parkService.discountInfoByScanCode(sign, discountDTO);
    }

    /**
     * 根据设备编号查询项目信息.
     * @param sign C 端 使用 deviceNo 进行签名制作.
     * @param projectQueryDTO {@link ProjectQueryDTO}
     * @return {@link SpkCommonResult}
     */
    @PostMapping("projectInfoByDeviceNo")
    public SpkCommonResult projectInfoByDeviceNo(@RequestHeader("sign") final String sign, @RequestBody final ProjectQueryDTO projectQueryDTO) {
        return parkService.projectInfoByDeviceNo(sign, projectQueryDTO);
    }

    /**
     * 根据设备编号查询项目信息.
     * @param sign C 端 使用 projectNo 进行签名制作.
     * @param projectInfoQueryDTO {@link ProjectInfoQueryDTO}
     * @return {@link SpkCommonResult}
     */
    @PostMapping("projectInfoByProjectNo")
    public SpkCommonResult projectInfoByProjectNo(@RequestHeader("sign") final String sign, @RequestBody final ProjectInfoQueryDTO projectInfoQueryDTO) {
        return parkService.projectInfoByProjectNo(sign, projectInfoQueryDTO);
    }

    /**
     * 根据项目编号和设备编号查询设备编号.
     * @param sign C 端 使用 projectNo 进行签名制作.
     * @param projectInfoQueryDTO {@link ProjectInfoQueryDTO}
     * @return {@link SpkCommonResult}
     */
    @PostMapping("getDeviceNo")
    public SpkCommonResult getDeviceNo(@RequestHeader("sign") final String sign, @RequestBody final ProjectInfoQueryDTO projectInfoQueryDTO) {
        return parkService.getDeviceNo(sign, projectInfoQueryDTO);
    }
}
