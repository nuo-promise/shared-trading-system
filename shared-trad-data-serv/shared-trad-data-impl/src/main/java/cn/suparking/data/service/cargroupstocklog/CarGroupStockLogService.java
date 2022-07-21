package cn.suparking.data.service.cargroupstocklog;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.data.api.beans.cargroupstocklog.CarGroupStockOperateRecordQueryDTO;
import cn.suparking.data.dao.entity.cargroupstocklog.CarGroupStockOperateRecordDO;

public interface CarGroupStockLogService {

    /**
     * 创建合约库存操作记录.
     *
     * @param carGroupStockOperateRecordDO {@link CarGroupStockOperateRecordDO}
     * @return {@linkplain SpkCommonResult}
     */
    SpkCommonResult create(CarGroupStockOperateRecordDO carGroupStockOperateRecordDO);

    /**
     * 获取合约库存操作记录列表.
     *
     * @param operateRecordQueryDTO {@link CarGroupStockOperateRecordQueryDTO}
     * @return {@linkplain SpkCommonResult}
     */
    SpkCommonResult list(CarGroupStockOperateRecordQueryDTO operateRecordQueryDTO);
}
