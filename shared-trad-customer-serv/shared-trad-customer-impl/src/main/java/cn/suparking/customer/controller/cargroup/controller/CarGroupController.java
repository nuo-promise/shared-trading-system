package cn.suparking.customer.controller.cargroup.controller;

import cn.suparking.customer.controller.cargroup.service.CarGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RefreshScope
@RestController
@RequestMapping("car-group-api")
public class CarGroupController {

    private final CarGroupService carGroupService;

    public CarGroupController(final CarGroupService carGroupService) {
        this.carGroupService = carGroupService;
    }

}
