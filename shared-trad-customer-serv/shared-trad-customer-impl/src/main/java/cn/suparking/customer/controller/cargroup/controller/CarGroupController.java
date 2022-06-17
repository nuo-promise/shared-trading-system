package cn.suparking.customer.controller.cargroup.controller;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.common.api.utils.SpkCommonAssert;
import cn.suparking.common.api.utils.SpkCommonResultMessage;
import cn.suparking.customer.api.beans.cargroup.CarGroupDTO;
import cn.suparking.customer.api.beans.cargroup.CarGroupQueryDTO;
import cn.suparking.customer.controller.cargroup.service.CarGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@RefreshScope
@RestController
@RequestMapping("car-group-api")
public class CarGroupController {

    private final CarGroupService carGroupService;

    public CarGroupController(final CarGroupService carGroupService) {
        this.carGroupService = carGroupService;
    }

    /**
     * 合约列表.
     *
     * @param carGroupQueryDTO {@link CarGroupQueryDTO}
     * @return {@link List}
     */
    @PostMapping("list")
    public SpkCommonResult list(@Valid @RequestBody final CarGroupQueryDTO carGroupQueryDTO) {
        return SpkCommonResult.success(carGroupService.list(carGroupQueryDTO));
    }

    /**
     * 合约添加.
     *
     * @param carGroupDTO {@link CarGroupDTO}
     * @return {@link List}
     */
    @PostMapping("save")
    public SpkCommonResult insert(@Valid @RequestBody final CarGroupDTO carGroupDTO) {
        return Optional.ofNullable(carGroupDTO)
                .map(item -> {
                    return carGroupService.insert(carGroupDTO);
                }).orElseGet(() -> SpkCommonResult.error(SpkCommonResultMessage.PARAMETER_ERROR + " pageSize, pageNum 不能为 null"));
    }

    /**
     * 根据id查询合约信息.
     *
     * @param carGroupDTO {@link CarGroupDTO}
     * @return {@link List}
     */
    @PostMapping("findById")
    public SpkCommonResult findById(@Valid @RequestBody final CarGroupDTO carGroupDTO) {
        return Optional.ofNullable(carGroupDTO)
                .map(item -> {
                    SpkCommonAssert.notNull(item.getId(), "合约id不能为空");
                    return carGroupService.findById(carGroupDTO);
                }).orElseGet(() -> SpkCommonResult.error("合约id不能为空"));
    }

    /**
     * 合约删除.
     *
     * @param carGroupDTO {@link CarGroupDTO}
     * @return {@link List}
     */
    @PostMapping("remove")
    public SpkCommonResult remove(@Valid @RequestBody final CarGroupDTO carGroupDTO) {
        return Optional.ofNullable(carGroupDTO)
                .map(item -> {
                    SpkCommonAssert.notNull(item.getId(), "合约id不能为空");
                    return carGroupService.remove(carGroupDTO);
                }).orElseGet(() -> SpkCommonResult.error("合约id不能为空"));
    }
}
