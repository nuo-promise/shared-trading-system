package cn.suparking.user.controller;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.common.api.utils.SpkCommonAssert;
import cn.suparking.common.api.utils.SpkCommonResultMessage;
import cn.suparking.user.api.beans.CarParkDTO;
import cn.suparking.user.service.intf.CarParkService;
import cn.suparking.user.vo.CarParkVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

/**
 * Car park Controller.
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("car-park")
public class CarParkController {

    private final CarParkService carParkService;

    public CarParkController(final CarParkService carParkService) {
        this.carParkService = carParkService;
    }

    /**
     * create car park.
     * @param carParkDTO {@linkplain CarParkDTO}
     * @return {@linkplain SpkCommonResult}
     */
    @PostMapping("")
    public SpkCommonResult createCarPark(@Valid @RequestBody final CarParkDTO carParkDTO) {
        return Optional.ofNullable(carParkDTO)
                .map(item -> {
                    SpkCommonAssert.notBlank(item.getUserId(), SpkCommonResultMessage.PARAMETER_ERROR + ": user id is not blank");
                    SpkCommonAssert.notBlank(item.getParkNo(), SpkCommonResultMessage.PARAMETER_ERROR + ": park no is not blank");
                    SpkCommonAssert.notBlank(item.getAddress(), SpkCommonResultMessage.PARAMETER_ERROR + ": address is not blank");
                    SpkCommonAssert.notBlank(item.getParkId(), SpkCommonResultMessage.PARAMETER_ERROR + ": park id is not blank");
                    Integer createCount = carParkService.createOrUpdate(carParkDTO);
                    return SpkCommonResult.success(SpkCommonResultMessage.CREATE_SUCCESS, createCount);
                }).orElseGet(() -> SpkCommonResult.error(SpkCommonResultMessage.CAR_PARK_CREATE_USER_ERROR));
    }

    /**
     * detail carPark.
     *
     * @param id car Park id.
     * @return {@linkplain SpkCommonResult}
     */
    @GetMapping("/{id}")
    public SpkCommonResult detailUser(@PathVariable("id") final Long id) {
        CarParkVO carParkVO = carParkService.findById(id);
        return Optional.ofNullable(carParkVO)
                .map(item -> SpkCommonResult.success(SpkCommonResultMessage.DETAIL_SUCCESS, item))
                .orElseGet(() -> SpkCommonResult.error(SpkCommonResultMessage.CAR_PARK_QUERY_ERROR));
    }
}
