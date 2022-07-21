package cn.suparking.customer.controller.cargroup.controller;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.common.api.utils.SpkCommonAssert;
import cn.suparking.common.api.utils.SpkCommonResultMessage;
import cn.suparking.customer.api.beans.cargroup.VipCarQueryDTO;
import cn.suparking.customer.controller.cargroup.service.MyVipCarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

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
     * @param vipCarQueryDTO {@linkplain VipCarQueryDTO}
     * @return {@link SpkCommonResult}
     * @author ZDD
     * @date 2022/7/20 14:53:11
     */
    @PostMapping("list")
    public SpkCommonResult myVipCarList(@RequestBody VipCarQueryDTO vipCarQueryDTO) {
        return Optional.ofNullable(vipCarQueryDTO).map(item -> {
            SpkCommonAssert.notBlank(vipCarQueryDTO.getUserId(), SpkCommonResultMessage.PARAMETER_ERROR + " userId 不能为空");
            return SpkCommonResult.success(SpkCommonResultMessage.RESULT_SUCCESS, myVipCarService.myVipCarList(item));
        }).orElseGet(() -> SpkCommonResult.error(SpkCommonResultMessage.PARAMETER_ERROR));
    }

    /**
     * 获取线上所有可办合约的场库列表.
     *
     * @return {@link SpkCommonResult}
     * @author ZDD
     * @date 2022/7/20 14:53:11
     */
    @GetMapping("projectVipCarList")
    public SpkCommonResult projectVipCarList() {
        return SpkCommonResult.success(SpkCommonResultMessage.RESULT_SUCCESS, myVipCarService.projectVipCarList());
    }

    /**
     * 获取线上所有可办合约列表.
     *
     * @param projectNo 场库编号
     * @return {@link SpkCommonResult}
     * @author ZDD
     * @date 2022/7/20 14:53:11
     */
    @GetMapping("protocolVipCarList")
    public SpkCommonResult protocolVipCarList(@RequestParam String projectNo) {
        return SpkCommonResult.success(SpkCommonResultMessage.RESULT_SUCCESS, myVipCarService.protocolVipCarList(projectNo));
    }
}
