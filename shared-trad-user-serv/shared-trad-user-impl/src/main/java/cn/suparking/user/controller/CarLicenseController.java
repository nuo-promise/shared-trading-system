package cn.suparking.user.controller;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.common.api.utils.SpkCommonAssert;
import cn.suparking.common.api.utils.SpkCommonResultMessage;
import cn.suparking.user.api.beans.CarLicenseDTO;
import cn.suparking.user.service.intf.CarLicenseService;
import cn.suparking.user.vo.CarLicenseVO;
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

@Slf4j
@RefreshScope
@RestController
@RequestMapping("car-license")
public class CarLicenseController {

    private final CarLicenseService carLicenseService;

    public CarLicenseController(final CarLicenseService carLicenseService) {
        this.carLicenseService = carLicenseService;
    }

    /**
     * create car license.
     * @param carLicenseDTO {@linkplain CarLicenseDTO}
     * @return {@linkplain SpkCommonResult}
     */
    @PostMapping("")
    public SpkCommonResult createCarLicense(@Valid @RequestBody final CarLicenseDTO carLicenseDTO) {
        return Optional.ofNullable(carLicenseDTO)
                .map(item -> {
                    SpkCommonAssert.notBlank(item.getUserId(), SpkCommonResultMessage.PARAMETER_ERROR + ": user id is not blank");
                    SpkCommonAssert.notBlank(item.getCarLicense(), SpkCommonResultMessage.PARAMETER_ERROR + ": car license is not blank");
                    SpkCommonAssert.notNull(item.getType(), SpkCommonResultMessage.PARAMETER_ERROR + ": type is not null");
                    Integer createCount = carLicenseService.createOrUpdate(carLicenseDTO);
                    return SpkCommonResult.success(SpkCommonResultMessage.CREATE_SUCCESS, createCount);
                }).orElseGet(() -> SpkCommonResult.error(SpkCommonResultMessage.CAR_LICENSE_CREATE_USER_ERROR));
    }

    /**
     * detail car license.
     *
     * @param id car Park id.
     * @return {@linkplain SpkCommonResult}
     */
    @GetMapping("/{id}")
    public SpkCommonResult detailCarLicense(@PathVariable("id") final Long id) {
        CarLicenseVO carLicenseVO = carLicenseService.findById(id);
        return Optional.ofNullable(carLicenseVO)
                .map(item -> SpkCommonResult.success(SpkCommonResultMessage.DETAIL_SUCCESS, item))
                .orElseGet(() -> SpkCommonResult.error(SpkCommonResultMessage.CAR_LICENSE_QUERY_ERROR));
    }
}
