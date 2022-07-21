package cn.suparking.customer.controller.cargroupstock.service.impl;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.common.api.configuration.SnowflakeConfig;
import cn.suparking.common.api.exception.SpkCommonException;
import cn.suparking.customer.api.beans.cargroupstock.CarGroupStockOperateRecordDTO;
import cn.suparking.customer.api.beans.cargroupstock.CarGroupStockOperateRecordQueryDTO;
import cn.suparking.customer.api.beans.cargroupstock.CarGroupStockQueryDTO;
import cn.suparking.customer.controller.cargroupstock.service.CarGroupStockService;
import cn.suparking.customer.dao.entity.CarGroupStockDO;
import cn.suparking.customer.dao.mapper.CarGroupStockMapper;
import cn.suparking.customer.feign.data.DataTemplateService;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class CarGroupStockServiceImpl implements CarGroupStockService {

    private final CarGroupStockMapper carGroupStockMapper;

    private final DataTemplateService dataTemplateService;

    public CarGroupStockServiceImpl(CarGroupStockMapper carGroupStockMapper, DataTemplateService dataTemplateService) {
        this.carGroupStockMapper = carGroupStockMapper;
        this.dataTemplateService = dataTemplateService;
    }

    /**
     * 获取合约协议库存列表.
     *
     * @param carGroupStockQueryDTO {@link CarGroupStockQueryDTO}
     * @return {@linkplain SpkCommonResult}
     */
    @Override
    public SpkCommonResult list(CarGroupStockQueryDTO carGroupStockQueryDTO) {
        log.info("[合约库存]-获取库存列表 ======> 请求参数 = [{}]", carGroupStockQueryDTO);
        PageHelper.startPage(carGroupStockQueryDTO.getPage(), carGroupStockQueryDTO.getSize());
        List<CarGroupStockDO> carGroupStockList = carGroupStockMapper.list(carGroupStockQueryDTO);
        PageInfo<CarGroupStockDO> carGroupVOPageInfo = new PageInfo<>(carGroupStockList);
        log.info("[合约库存]-获取库存列表 <====== 请求成功 SUCCESS [{}]", carGroupVOPageInfo);
        return SpkCommonResult.success(carGroupVOPageInfo);
    }

    /**
     * 新增合约库存.
     *
     * @param carGroupStock {@link CarGroupStockDO}
     * @return {@linkplain SpkCommonResult}
     */
    @Override
    public SpkCommonResult insert(CarGroupStockDO carGroupStock) {
        log.info("[合约库存]-新增合约库存 ======> 请求参数 = [{}]", carGroupStock);

        String projectNo = carGroupStock.getProjectNo();
        String protocolId = carGroupStock.getProtocolId();
        Integer stockQuantity = carGroupStock.getStockQuantity() == null ? 0 : carGroupStock.getStockQuantity();

        /**
         * 判断是否已经有该合约的库存 ======> 如果有则不建立库存失败
         */
        CarGroupStockQueryDTO carGroupStockQueryDTO = CarGroupStockQueryDTO.builder().projectNos(Arrays.asList(projectNo))
                .protocolId(protocolId)
                .build();
        List<CarGroupStockDO> groupStockDOList = carGroupStockMapper.list(carGroupStockQueryDTO);
        if (!CollectionUtils.isEmpty(groupStockDOList)) {
            log.warn("[合约库存]-新增合约库存 <====== 新建失败, 车场[{}] 合约协议[{}] 已存在库存", projectNo, protocolId);
            return SpkCommonResult.error("该场库已存在该合约库存");
        }

        /**
         * 开始赋值合约库存实体
         */
        carGroupStock.setId(SnowflakeConfig.snowflakeId());
        carGroupStock.setCumulativeIn(carGroupStock.getStockQuantity());
        carGroupStock.setDateCreated(new Timestamp(System.currentTimeMillis()));
        carGroupStock.setDateUpdated(new Timestamp(System.currentTimeMillis()));
        carGroupStockMapper.insert(carGroupStock);

        if (stockQuantity > 0) {
            CarGroupStockOperateRecordDTO record = new CarGroupStockOperateRecordDTO();
            record.logIn(carGroupStock.getId(), stockQuantity, carGroupStock.getCreator(), "901", null);
            SpkCommonResult spkCommonResult = dataTemplateService.carGroupStockLogInsert(record);
            if (spkCommonResult == null || spkCommonResult.getCode() != 200) {
                throw new SpkCommonException("请求新增合约操作记录失败");
            }
        }

        log.info("[合约库存]-新增合约库存 <====== 请求成功 SUCCESS");
        return SpkCommonResult.success(carGroupStock);
    }

    /**
     * 操作合约库存.
     *
     * @param carGroupStockQueryDTO {@link CarGroupStockQueryDTO}
     * @return {@linkplain SpkCommonResult}
     */
    @Override
    public SpkCommonResult operate(CarGroupStockQueryDTO carGroupStockQueryDTO) {
        log.info("[合约库存]-操作合约库存 ======> 请求参数 = [{}]", JSONObject.toJSONString(carGroupStockQueryDTO));

        String operateType = carGroupStockQueryDTO.getOperateType() == null ? "INCREASE" : carGroupStockQueryDTO.getOperateType();
        Integer quantity = carGroupStockQueryDTO.getQuantity() == null ? 0 : carGroupStockQueryDTO.getQuantity();
        Long id = carGroupStockQueryDTO.getId();
        String operator = carGroupStockQueryDTO.getModifier();
        String remark = carGroupStockQueryDTO.getRemark();

        if (id == null || operator == null) {
            log.warn("[合约库存]-操作合约库存 <====== 请求失败, 库存id / 操作人为null");
            return SpkCommonResult.error("库存id / 操作人为null");
        }

        CarGroupStockDO carGroupStock = carGroupStockMapper.findById(id);
        if (carGroupStock == null) {
            log.warn("[合约库存]-操作合约库存 <====== 请求失败, id[{}]无对应库存数据", id);
            return SpkCommonResult.error("该合约库存记录不存在");
        }

        //如果操作值为0则直接返回即可 无需生成操作记录
        if (quantity == 0) {
            return SpkCommonResult.success(carGroupStock);
        }

        /**
         * 开始执行操作
         */
        if ("INCREASE".equals(operateType)) {
            //增加库存操作(累加库存数量 + 累加入库数量)
            carGroupStock.doIn(quantity, operator);
            carGroupStockMapper.update(carGroupStock);

            //记录入库操作
            CarGroupStockOperateRecordDTO record = new CarGroupStockOperateRecordDTO();
            record.logIn(carGroupStock.getId(), quantity, operator, "901", remark);
            dataTemplateService.carGroupStockLogInsert(record);
            return SpkCommonResult.success(carGroupStock);
        }

        carGroupStock.doOut(quantity, operator);
        carGroupStockMapper.update(carGroupStock);

        //记录人为减库操作
        CarGroupStockOperateRecordDTO record = new CarGroupStockOperateRecordDTO();
        record.logOut(carGroupStock.getId(), quantity, operator, "901", remark);
        dataTemplateService.carGroupStockLogInsert(record);
        return SpkCommonResult.success(carGroupStock);
    }

    /**
     * 获取合约库存操作记录列表.
     *
     * @param carGroupStockOperateRecordQueryDTO {@link CarGroupStockOperateRecordDTO}
     * @return {@linkplain SpkCommonResult}
     */
    @Override
    public SpkCommonResult logList(CarGroupStockOperateRecordQueryDTO carGroupStockOperateRecordQueryDTO) {
        log.info("[合约库存]-获取合约库存操作记录 ======> 请求参数 = [{}]", JSONObject.toJSONString(carGroupStockOperateRecordQueryDTO));
        JSONObject result = new JSONObject();

        Long stockId = carGroupStockOperateRecordQueryDTO.getStockId();
        if (stockId == null) {
            log.warn("[合约库存]-获取合约库存操作记录 <====== 请求失败, 库存id为null");
            return SpkCommonResult.error("未选择合约库存");
        }

        SpkCommonResult spkCommonResult = dataTemplateService.carGroupStockLogList(carGroupStockOperateRecordQueryDTO);
        Integer code = spkCommonResult.getCode();
        if (code != 200) {
            log.warn("[合约库存]-获取合约库存操作记录 <====== 请求失败 [{}]", JSONObject.toJSONString(spkCommonResult));
            SpkCommonResult.error("获取合约库存操作记录失败");
        }
        Object data = spkCommonResult.getData();
        if (!ObjectUtils.isEmpty(data)) {
            PageInfo pageInfo = JSONObject.parseObject(JSONObject.toJSONString(data),PageInfo.class);
            result.put("total", String.valueOf(pageInfo.getTotal()));
            result.put("list", JSONObject.toJSONString(pageInfo.getList()));
        }

        log.info("[合约库存]-获取合约库存操作记录 <====== 请求成功 SUCCESS = [{}]", result);
        return SpkCommonResult.success(result);
    }
}
