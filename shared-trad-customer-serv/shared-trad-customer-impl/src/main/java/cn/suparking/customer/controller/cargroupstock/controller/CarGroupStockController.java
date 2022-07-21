/**
 * Description:TODO
 *
 * @author ZDD
 * @date 2022-06-23 12:10:47
 */
package cn.suparking.customer.controller.cargroupstock.controller;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.common.api.utils.SpkCommonAssert;
import cn.suparking.common.api.utils.SpkCommonResultMessage;
import cn.suparking.customer.api.beans.cargroupstock.CarGroupStockOperateRecordQueryDTO;
import cn.suparking.customer.api.beans.cargroupstock.CarGroupStockQueryDTO;
import cn.suparking.customer.controller.cargroupstock.service.CarGroupStockService;
import cn.suparking.customer.dao.entity.CarGroupStockDO;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@Slf4j
@RefreshScope
@RestController
@RequestMapping("/car-group-stock")
public class CarGroupStockController {

    private final CarGroupStockService carGroupStockService;

    public CarGroupStockController(CarGroupStockService carGroupStockService) {
        this.carGroupStockService = carGroupStockService;
    }

    /**
     * 获取合约协议库存列表.
     *
     * @param carGroupStockQueryDTO {@link CarGroupStockQueryDTO}
     * @return {@linkplain SpkCommonResult}
     */
    @PostMapping("/list")
    public SpkCommonResult list(@Valid @RequestBody final CarGroupStockQueryDTO carGroupStockQueryDTO) {
        return carGroupStockService.list(carGroupStockQueryDTO);
    }

    /**
     * 新增合约库存.
     *
     * @param carGroupStock {@link CarGroupStockDO}
     * @return {@linkplain SpkCommonResult}
     */
    @PostMapping("insert")
    @GlobalTransactional(name = "shared-trad-customer-serv", rollbackFor = Exception.class)
    public SpkCommonResult insert(@RequestBody CarGroupStockDO carGroupStock) {
        return Optional.ofNullable(carGroupStock)
                .map(item -> {
                    SpkCommonAssert.notNull(item.getProjectNo(), "请选择车场");
                    SpkCommonAssert.notNull(item.getProtocolId(), "请选择合约");
                    return carGroupStockService.insert(carGroupStock);
                }).orElseGet(() -> SpkCommonResult.error(SpkCommonResultMessage.PARAMETER_ERROR));
    }

    /**
     * 操作合约库存.
     *
     * @param carGroupStockQueryDTO {@link CarGroupStockQueryDTO}
     * @return {@linkplain SpkCommonResult}
     */
    @PostMapping("operate")
    @GlobalTransactional(name = "shared-trad-customer-serv", rollbackFor = Exception.class)
    public SpkCommonResult operate(@RequestBody CarGroupStockQueryDTO carGroupStockQueryDTO) {
        return Optional.ofNullable(carGroupStockQueryDTO)
                .map(item -> {
                    SpkCommonAssert.notNull(item.getId(), "请选择操作合约");
                    return carGroupStockService.operate(carGroupStockQueryDTO);
                }).orElseGet(() -> SpkCommonResult.error(SpkCommonResultMessage.PARAMETER_ERROR));
    }

    /**
     * 获取合约库存操作记录列表.
     *
     * @param carGroupStockOperateRecordQueryDTO {@link CarGroupStockOperateRecordQueryDTO}
     * @return {@linkplain SpkCommonResult}
     */
    @PostMapping("logList")
    public SpkCommonResult logList(@RequestBody CarGroupStockOperateRecordQueryDTO carGroupStockOperateRecordQueryDTO) {
        return carGroupStockService.logList(carGroupStockOperateRecordQueryDTO);
    }
}
