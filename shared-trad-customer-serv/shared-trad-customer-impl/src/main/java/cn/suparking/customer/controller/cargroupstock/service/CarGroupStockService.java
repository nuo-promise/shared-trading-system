package cn.suparking.customer.controller.cargroupstock.service;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.customer.api.beans.cargroupstock.CarGroupStockOperateRecordQueryDTO;
import cn.suparking.customer.api.beans.cargroupstock.CarGroupStockQueryDTO;
import cn.suparking.customer.dao.entity.CarGroupStockDO;

public interface CarGroupStockService {

    /**
     * 获取合约协议库存列表.
     *
     * @param carGroupStockQueryDTO {@link CarGroupStockQueryDTO}
     * @return {@linkplain SpkCommonResult}
     */
    SpkCommonResult list(CarGroupStockQueryDTO carGroupStockQueryDTO);

    /**
     * 新增合约库存.
     *
     * @param carGroupStock {@link CarGroupStockDO}
     * @return {@linkplain SpkCommonResult}
     */
    SpkCommonResult insert(CarGroupStockDO carGroupStock);

    /**
     * 新增合约库存.
     *
     * @param carGroupStockQueryDTO {@link CarGroupStockQueryDTO}
     * @return {@linkplain SpkCommonResult}
     */
    SpkCommonResult operate(CarGroupStockQueryDTO carGroupStockQueryDTO);

    /**
     * 获取合约库存操作记录列表.
     *
     * @param carGroupStockOperateRecordQueryDTO {@link CarGroupStockOperateRecordQueryDTO}
     * @return {@linkplain SpkCommonResult}
     */
    SpkCommonResult logList(CarGroupStockOperateRecordQueryDTO carGroupStockOperateRecordQueryDTO);
}
