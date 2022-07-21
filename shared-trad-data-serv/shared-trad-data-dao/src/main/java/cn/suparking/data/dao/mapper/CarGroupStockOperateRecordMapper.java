package cn.suparking.data.dao.mapper;

import cn.suparking.data.api.beans.cargroupstocklog.CarGroupStockOperateRecordQueryDTO;
import cn.suparking.data.dao.entity.cargroupstocklog.CarGroupStockOperateRecordDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CarGroupStockOperateRecordMapper {
    /**
     * 存操作记录列表.
     *
     * @param operateRecordQueryDTO {@link CarGroupStockOperateRecordQueryDTO}
     * @return {@linkplain CarGroupStockOperateRecordDO}
     */
    List<CarGroupStockOperateRecordDO> list(CarGroupStockOperateRecordQueryDTO operateRecordQueryDTO);

    /**
     * 保存库存操作记录.
     *
     * @param carGroupStockOperateRecordDO {@link CarGroupStockOperateRecordDO}
     * @return int
     */
    int insert(CarGroupStockOperateRecordDO carGroupStockOperateRecordDO);
}
