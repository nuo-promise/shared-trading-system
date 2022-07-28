package cn.suparking.customer.controller.cargroup.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.suparking.common.api.exception.SpkCommonException;
import cn.suparking.common.api.utils.DateUtils;
import cn.suparking.common.api.utils.HttpRequestUtils;
import cn.suparking.customer.api.beans.cargroup.CarGroupDTO;
import cn.suparking.customer.api.beans.cargroup.CarGroupPeriodDTO;
import cn.suparking.customer.api.beans.vip.VipPayDTO;
import cn.suparking.customer.api.constant.ParkConstant;
import cn.suparking.customer.configuration.properties.SparkProperties;
import cn.suparking.customer.controller.cargroup.service.CarGroupService;
import cn.suparking.customer.dao.entity.CarGroupPeriod;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class CarGroupServiceImpl implements CarGroupService {

    @Resource
    private SparkProperties sparkProperties;

    /**
     * 合约数据校验(新增 NEW/ 续费 RENEW).
     *
     * @param vipPayDTO {@linkplain VipPayDTO}
     * @param type      办理类型:新增 NEW/ 续费 RENEW
     * @return Boolean
     * @author ZDD
     * @date 2022/7/22 15:59:28
     */
    @Override
    public CarGroupDTO check(final VipPayDTO vipPayDTO, final String type) {
        String projectNo = vipPayDTO.getProjectNo();
        Long userId = Long.valueOf(vipPayDTO.getUserId());
        if (Objects.isNull(type) || Objects.isNull(projectNo)) {
            log.warn("合约数据校验失败 <====== 请求参数有误, 办理类型无法确定!");
            throw new SpkCommonException("必填参数缺失!");
        }
        if (!ParkConstant.NEW.equals(type) && !ParkConstant.RENEW.equals(type)) {
            log.warn("合约数据校验失败 <====== 请求参数有误, 办理类型type[{}]无法识别!", type);
            throw new SpkCommonException("办理业务类型无法识别!");
        }

        //合约新办
        if (ParkConstant.NEW.equals(type)) {
            Map<String, Object> carGroupOneParams = new HashMap<>();
            CarGroupDTO carGroupDTO = CarGroupDTO.builder().projectNo(projectNo).userId(userId).build();
            carGroupOneParams.put("userId", carGroupDTO.getUserId());
            carGroupOneParams.put("projectNo", carGroupDTO.getProjectNo());
            JSONObject carGroupOneResult = HttpRequestUtils.sendPost(sparkProperties.getUrl() + ParkConstant.INTERFACE_FIND_BY_USERID_PROJECTNO, carGroupOneParams);
            if (Objects.isNull(carGroupOneResult) || !ParkConstant.SUCCESS.equals(carGroupOneResult.getString("code"))) {
                log.warn("请求获取合约信息失败 [{}]", JSONObject.toJSONString(carGroupOneResult));
                throw new SpkCommonException("请求获取合约信息失败!");
            }

            JSONObject onlineCarGroup = carGroupOneResult.getJSONObject("onlineCarGroup");
            if (Objects.nonNull(onlineCarGroup)) {
                //表示该用户在该场库已经存在合约 ======> 无法进行新增操作
                log.warn("新增合约数据校验 <====== 校验失败, 用户[{}] 在场库 [{}] 已存在合约!", userId, projectNo);
                throw new SpkCommonException("请求新增合约数据校验，用户在该场库已存在合约!");
            }

            String protocolId = vipPayDTO.getProtocolId();
            Integer dueAmount = vipPayDTO.getDueAmount();

            Map<String, Object> params = new HashMap<>();
            params.put("protocolId", protocolId);
            JSONObject result = HttpRequestUtils.sendGet(sparkProperties.getUrl() + ParkConstant.INTERFACE_MYVIPCARINFO, params);
            if (Objects.isNull(result) || !ParkConstant.SUCCESS.equals(result.getString("code"))) {
                log.warn("协议id [{}] 无对应协议, 属于数据异常", protocolId);
                throw new SpkCommonException("获取合约协议失败!");
            }
            JSONObject protocol = result.getJSONObject("protocol");
            if (Objects.isNull(protocol)) {
                log.warn("用户[{}]请求办理新增合约 <====== 请求失败, id[{}]未找到对应的租约协议数据, 属于数据异常!", userId, protocolId);
                throw new SpkCommonException("获取合约协议不存在!");
            }

            if (ParkConstant.TIME_BASED.equals(protocol.getString("protocolType"))) {
                //如果是时效协议 ======> 需要判断提交的参数中是否带了租期
                if (Objects.isNull(dueAmount)) {
                    log.warn("用户[{}]请求办理新增合约 <====== 请求失败, 办理时效租约必填参数缺失!", userId);
                    throw new SpkCommonException("合约有效期有误!");
                }
            }

            //设置车辆类型id
            JSONObject carType = result.getJSONObject("carType");
            if (Objects.isNull(carType)) {
                log.warn("用户[{}]请求办理新增合约 <====== 请求失败, id[{}]未找到对应的车辆类型数据, 属于数据异常!", userId, protocolId);
                throw new SpkCommonException("协议车辆类型不存在!");
            }

            //组织合约数据并返回
            CarGroupDTO carGroup = createCarGroup(vipPayDTO, protocol, carType);
            //预插入无效合约
            Map<String, Object> carGroupParams = new HashMap<>();
            JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(carGroup));
            for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                carGroupParams.put(entry.getKey(), entry.getValue());
            }

            JSONObject carGroupResult = HttpRequestUtils.sendPost(sparkProperties.getUrl() + ParkConstant.INTERFACE_INSERT, carGroupParams);
            if (Objects.isNull(carGroupResult) || !ParkConstant.SUCCESS.equals(carGroupResult.getString("code")) || Objects.isNull(carGroupResult.getJSONObject("carGroup"))) {
                log.warn("预插入合约失败  [{}]", JSONObject.toJSONString(carGroupResult));
                return null;
            }
            String id = carGroupResult.getJSONObject("carGroup").getString("id");
            carGroup.setId(id);
            return carGroup;
        }

        //合约续费
        if (ParkConstant.RENEW.equals(type)) {
            log.info("用户[{}]请求续费合约数据校验 ======> 请求参数 = [{}]", userId, JSONObject.toJSONString(vipPayDTO));
            //想要续费的合约id ======> 必须存在
            String id = vipPayDTO.getCarGroupId();

            if (Objects.isNull(id) || Objects.isNull(vipPayDTO.getBeginDate()) || Objects.isNull(vipPayDTO.getEndDate())) {
                log.warn("用户[{}]请求续费合约数据校验 <====== 请求失败, 续费合约必填参数缺失!", userId);
                throw new SpkCommonException("必填参数缺失!");
            }

            long beginDate = DateUtils.getMillByDateStartStr(vipPayDTO.getBeginDate());
            long endDate = DateUtils.getMillByDateEndStr(vipPayDTO.getEndDate());

            //想要续费的合约
            Map<String, Object> params = new HashMap<>();
            params.put("id", id);
            JSONObject result = HttpRequestUtils.sendGet(sparkProperties.getUrl() + ParkConstant.INTERFACE_FINDBYID, params);
            if (Objects.isNull(result) || !ParkConstant.SUCCESS.equals(result.getString("code")) || Objects.isNull(result.getJSONObject("onlineCarGroup"))) {
                log.warn("用户[{}]请求续费合约数据校验 <====== 请求失败, 续费合约id[{}]无法找到对应的合约!", userId, id);
                throw new SpkCommonException("无法找到对应的合约!");
            }

            JSONObject onlineCarGroup = result.getJSONObject("onlineCarGroup");
            JSONArray periods = onlineCarGroup.getJSONArray("periods");
            List<CarGroupPeriod> periodList = new ArrayList<>();
            //获取到之前的合约时间段
            for (int i = 0; i < periods.size(); i++) {
                CarGroupPeriod carGroupPeriod = CarGroupPeriod.builder().beginDate(periods.getJSONObject(i).getLong("beginDate")).endDate(periods.getJSONObject(i).getLong("endDate")).build();
                periodList.add(carGroupPeriod);
            }

            for (CarGroupPeriod period : periodList) {
                Long beginPeriod = period.getBeginDate();
                Long endPeriod = period.getEndDate();

                //前端输入的开始时间在某个时间段内
                if (beginDate >= beginPeriod && beginDate <= endPeriod) {
                    log.warn("用户[{}]请求续费合约数据校验 <====== 校验失败, 续费时间段有重合 <====== 合约时间段 = [{}] ~ [{}] "
                                    + "输入的时间段 = [{}] ~ [{}]", userId, DateUtil.formatDateTime(new Date(beginPeriod * 1000)),
                            DateUtil.formatDateTime(new Date(endPeriod * 1000)), DateUtil.formatDateTime(new Date(beginDate * 1000)),
                            DateUtil.formatDateTime(new Date(endDate * 1000)));
                    throw new SpkCommonException("选择的时间段有重合!");
                }

                //前端输入的结束时间在某个时间段内
                if (endDate >= beginPeriod && endDate <= endPeriod) {
                    log.warn("用户[{}]请求续费合约数据校验 <====== 校验失败, 续费时间段有重合 <====== 合约时间段 = [{}] ~ [{}] "
                                    + "输入的时间段 = [{}] ~ [{}]", userId, DateUtil.formatDateTime(new Date(beginPeriod * 1000)),
                            DateUtil.formatDateTime(new Date(endPeriod * 1000)), DateUtil.formatDateTime(new Date(beginDate * 1000)),
                            DateUtil.formatDateTime(new Date(endDate * 1000)));
                    throw new SpkCommonException("选择的时间段有重合!");
                }

                //前端输入的开始时间比某个时间段的开始时间小 并且 结束时间比这个时间段的结束时间大(指输入的时间段包含已有的时间段)
                if (beginDate < beginPeriod && endDate > endPeriod) {
                    log.warn("用户[{}]请求续费合约数据校验 <====== 校验失败, 续费时间段有重合 <====== 合约时间段 = [{}] ~ [{}] "
                                    + "输入的时间段 = [{}] ~ [{}]", userId, DateUtil.formatDateTime(new Date(endPeriod * 1000)),
                            DateUtil.formatDateTime(new Date(endPeriod * 1000)), DateUtil.formatDateTime(new Date(beginDate * 1000)),
                            DateUtil.formatDateTime(new Date(endDate * 1000)));
                    throw new SpkCommonException("选择的时间段有重合!");
                }
            }
            CarGroupPeriod carGroupPeriod = CarGroupPeriod.builder().beginDate(beginDate).endDate(endDate).build();
            return updateCarGroupMerge(onlineCarGroup, periodList, carGroupPeriod,userId);
        }
        return null;
    }

    private CarGroupDTO updateCarGroupMerge(final JSONObject onlineCarGroup, final List<CarGroupPeriod> periods, final CarGroupPeriod carGroupPeriod,final Long userId) {
        //从原合约中获取原租期列表
        periods.add(carGroupPeriod);
        //排序
        Collections.sort(periods);
        //融合以后的合约租期
        log.info("续约 合并后的periods ----> [{}]", periods);
        List<CarGroupPeriodDTO> merged = mergePeriod(periods);
        //将新的合约租期赋值给原先的合约carGroup
        CarGroupDTO carGroupDTO = CarGroupDTO.builder().id(onlineCarGroup.getString("id")).projectNo(onlineCarGroup.getString("projectNo"))
                .userId(userId).carTypeId(onlineCarGroup.getString("carTypeId"))
                .carTypeName(onlineCarGroup.getString("carTypeName")).protocolId(onlineCarGroup.getString("protocolId"))
                .protocolName(onlineCarGroup.getString("protocolName")).dateUpdated(new Timestamp(System.currentTimeMillis()))
                .userMobile(onlineCarGroup.getString("userMobile")).userName(onlineCarGroup.getString("userName")).modifier(ParkConstant.OPERATOR)
                .build();
        carGroupDTO.setCarGroupPeriodDTOList(merged);
        return carGroupDTO;
    }

    /**
     * 合并租期.
     *
     * @param periods {@link CarGroupPeriod}
     * @return {@link CarGroupPeriodDTO}
     * @author ZDD
     * @date 2022/7/26 15:31:03
     */
    private List<CarGroupPeriodDTO> mergePeriod(final List<CarGroupPeriod> periods) {
        List<CarGroupPeriodDTO> carGroupPeriodDTOList = new ArrayList<>();

        //合并方式：列表中的元素顺序比较 ======> 第一个和第二个进行合并处理 ======> 如果不能合并则将元素放入临时list中 ======> 如果到最后还是不能合并则输入临时list
        //如果可以合并则取第一个元素的下标对应删除临时list中的下标元素 ======> 合并数据
        // 将合并后的数据放入临时list中
        //重复执行（递归）
        if (periods.size() == 1) {
            CarGroupPeriod carGroupPeriod = periods.get(0);
            CarGroupPeriodDTO carGroupPeriodDTO = CarGroupPeriodDTO.builder().beginDate(carGroupPeriod.getBeginDate()).endDate(carGroupPeriod.getEndDate()).build();
            carGroupPeriodDTOList.add(carGroupPeriodDTO);
            return carGroupPeriodDTOList;
        }

        List<CarGroupPeriod> merged = new ArrayList<>();
        List<Integer> indexList = new ArrayList<>();

        for (int i = 1; i < periods.size(); i++) {
            int j = i;
            CarGroupPeriod first = periods.get(i - 1);
            CarGroupPeriod secend = periods.get(j);

            if (first.getEndDate() + 1 == secend.getBeginDate()) {
                if (merged.size() > 0) {
                    merged.remove(i - 1);
                }
                CarGroupPeriod merge = new CarGroupPeriod();
                merge.setBeginDate(first.getBeginDate());
                merge.setEndDate(secend.getEndDate());
                merged.add(merge);

                int nextIndex = j + 1;
                for (; nextIndex < periods.size(); nextIndex++) {
                    merged.add(periods.get(nextIndex));
                }

                Collections.sort(merged);
                return mergePeriod(merged);
            } else {
                if (!indexList.contains(i - 1)) {
                    merged.add(first);
                    indexList.add(i - 1);
                }

                if (!indexList.contains(j)) {
                    merged.add(secend);
                    indexList.add(j);
                }
            }
        }

        if (merged.size() == periods.size()) {
            for (CarGroupPeriod carGroupPeriod : merged) {
                CarGroupPeriodDTO groupPeriodDTO = CarGroupPeriodDTO.builder().beginDate(carGroupPeriod.getBeginDate()).endDate(carGroupPeriod.getEndDate()).build();
                carGroupPeriodDTOList.add(groupPeriodDTO);
            }
            return carGroupPeriodDTOList;
        } else {
            Collections.sort(merged);
            return mergePeriod(merged);
        }
    }

    /**
     * 组织新建合约数据.
     *
     * @param vipPayDTO {@linkplain VipPayDTO}
     * @param protocol  合约协议对象
     * @param carType   车辆类型对象
     * @return OnlineCarGroup
     * @author ZDD
     * @date 2022/7/22 16:29:23
     */
    private CarGroupDTO createCarGroup(final VipPayDTO vipPayDTO, final JSONObject protocol, final JSONObject carType) {
        String protocolType = protocol.getString("protocolType");
        Long beginDate = DateUtils.getMillByDateStartStr(vipPayDTO.getBeginDate());
        Long endDate = DateUtils.getMillByDateEndStr(vipPayDTO.getEndDate());

        CarGroupDTO carGroupDTO = CarGroupDTO.builder().carTypeId(carType.getString("id")).carTypeName(carType.getString("carTypeName"))
                .protocolId(vipPayDTO.getProtocolId()).protocolType(protocolType).protocolName(protocol.getString("protocolName"))
                .userMobile(vipPayDTO.getPhone()).userId(Long.valueOf(vipPayDTO.getUserId())).projectNo(vipPayDTO.getProjectNo())
                .beginDate(beginDate).endDate(endDate).dateCreated(new Timestamp(System.currentTimeMillis())).valid(false)
                .dateUpdated(new Timestamp(System.currentTimeMillis())).operator(ParkConstant.OPERATOR).modifier(ParkConstant.OPERATOR)
                .build();
        return carGroupDTO;
    }
}
