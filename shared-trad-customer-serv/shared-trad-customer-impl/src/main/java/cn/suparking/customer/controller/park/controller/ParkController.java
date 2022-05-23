package cn.suparking.customer.controller.park.controller;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.common.api.utils.SpkCommonAssert;
import cn.suparking.common.api.utils.SpkCommonResultMessage;
import cn.suparking.customer.beans.park.LocationDTO;
import cn.suparking.customer.controller.park.service.ParkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
                   return SpkCommonResult.success(parkService.nearByPark(item));
               }).orElseGet(() -> SpkCommonResult.error(SpkCommonResultMessage.PARAMETER_ERROR + " latitude, longitude 不能为 null"));
    }

    /**
     * 获取所有的厂库坐标打点.
     * @return {@link SpkCommonResult}
     */
    @GetMapping("allLocation")
    public SpkCommonResult allPark() {
        return SpkCommonResult.success(parkService.allLocation());
    }
}