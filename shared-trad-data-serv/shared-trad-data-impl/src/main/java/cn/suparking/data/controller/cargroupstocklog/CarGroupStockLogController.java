package cn.suparking.data.controller.cargroupstocklog;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.common.api.utils.SpkCommonResultMessage;
import cn.suparking.data.api.beans.cargroupstocklog.CarGroupStockOperateRecordQueryDTO;
import cn.suparking.data.dao.entity.cargroupstocklog.CarGroupStockOperateRecordDO;
import cn.suparking.data.service.cargroupstocklog.CarGroupStockLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.util.Asserts;
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
@RequestMapping("/car-group-stock-log")
public class CarGroupStockLogController {

    private final CarGroupStockLogService carGroupStockLogService;

    public CarGroupStockLogController(final CarGroupStockLogService carGroupStockLogService) {
        this.carGroupStockLogService = carGroupStockLogService;
    }

    /**
     * 获取合约库存操作记录列表.
     *
     * @param operateRecordQueryDTO {@link CarGroupStockOperateRecordQueryDTO}
     * @return {@linkplain SpkCommonResult}
     */
    @PostMapping("/list")
    public SpkCommonResult list(@Valid @RequestBody final CarGroupStockOperateRecordQueryDTO operateRecordQueryDTO) {
        return carGroupStockLogService.list(operateRecordQueryDTO);
    }

    /**
     * 创建合约库存操作记录.
     *
     * @param carGroupStockOperateRecordDO {@link CarGroupStockOperateRecordDO}
     * @return {@linkplain SpkCommonResult}
     */
    @PostMapping("/insert")
    public SpkCommonResult insert(@RequestBody final CarGroupStockOperateRecordDO carGroupStockOperateRecordDO) {
        return Optional.ofNullable(carGroupStockOperateRecordDO).map(item -> {
            Asserts.notNull(item.getStockId(), "合约库存id不能为空");
            Asserts.notNull(item.getOperateType(), "操作类型不能为空");
            Asserts.notNull(item.getQuantity(), "操作数量不能为空");
            return carGroupStockLogService.create(carGroupStockOperateRecordDO);
        }).orElseGet(() -> SpkCommonResult.error(SpkCommonResultMessage.PARAMETER_ERROR));
    }
}
