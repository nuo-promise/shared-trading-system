package cn.suparking.data.controller;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.data.api.beans.ParkConfigDTO;
import cn.suparking.data.service.ParkConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RefreshScope
@RestController
@RequestMapping("data-api")
public class ParkingController {

    private final ParkConfigService parkConfigService;

    public ParkingController(final ParkConfigService parkConfigService) {
        this.parkConfigService = parkConfigService;
    }


    /**
     * 项目配置信息变更通知.
     * @param parkSettingDTO {@link ParkConfigDTO}
     * @return {@link SpkCommonResult}
     */
    @PostMapping("/parkConfig")
    public SpkCommonResult parkingConfig(@Valid @RequestBody final ParkConfigDTO parkSettingDTO) {
        return parkConfigService.parkingConfig(parkSettingDTO);
    }
}
