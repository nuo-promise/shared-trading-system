package cn.suparking.data.service.cargroupstocklog.impl;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.common.api.exception.SpkCommonException;
import cn.suparking.data.api.beans.cargroupstocklog.CarGroupStockOperateRecordQueryDTO;
import cn.suparking.data.dao.entity.cargroupstocklog.CarGroupStockOperateRecordDO;
import cn.suparking.data.dao.mapper.CarGroupStockOperateRecordMapper;
import cn.suparking.data.service.cargroupstocklog.CarGroupStockLogService;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CarGroupStockLogServiceImpl implements CarGroupStockLogService {

    private final CarGroupStockOperateRecordMapper CarGroupStockOperateRecordMapper;

    public CarGroupStockLogServiceImpl(final CarGroupStockOperateRecordMapper carGroupStockOperateRecordMapper) {
        this.CarGroupStockOperateRecordMapper = carGroupStockOperateRecordMapper;
    }

    /**
     * 获取合约库存操作记录列表.
     *
     * @param operateRecordQueryDTO {@link CarGroupStockOperateRecordQueryDTO}
     * @return {@linkplain SpkCommonResult}
     */
    @Override
    public SpkCommonResult list(final CarGroupStockOperateRecordQueryDTO operateRecordQueryDTO) {
        PageHelper.startPage(operateRecordQueryDTO.getPage(), operateRecordQueryDTO.getSize());
        List<CarGroupStockOperateRecordDO> carGroupStockOperateRecordDOList = CarGroupStockOperateRecordMapper.list(operateRecordQueryDTO);
        PageInfo<CarGroupStockOperateRecordDO> pageInfo = new PageInfo<>(carGroupStockOperateRecordDOList);
        return SpkCommonResult.success(pageInfo);
    }

    /**
     * 创建合约库存操作记录.
     *
     * @param carGroupStockOperateRecordDO {@link CarGroupStockOperateRecordDO}
     * @return {@linkplain SpkCommonResult}
     */
    @Override
    public SpkCommonResult create(final CarGroupStockOperateRecordDO carGroupStockOperateRecordDO) {
        int count = CarGroupStockOperateRecordMapper.insert(carGroupStockOperateRecordDO);
        if (count <= 0) {
            log.warn("新增合约库存操作数据失败 [{}]", JSONObject.toJSONString(carGroupStockOperateRecordDO));
            throw new SpkCommonException("新增合约库存操作数据失败");
        }
        return SpkCommonResult.success();
    }
}
