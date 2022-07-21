package cn.suparking.customer.controller.cargroup.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.common.api.exception.SpkCommonException;
import cn.suparking.common.api.utils.HttpRequestUtils;
import cn.suparking.customer.api.beans.cargroup.VipCarQueryDTO;
import cn.suparking.customer.api.constant.ParkConstant;
import cn.suparking.customer.configuration.properties.SparkProperties;
import cn.suparking.customer.controller.cargroup.service.MyVipCarService;
import cn.suparking.customer.dao.entity.CarGroup;
import cn.suparking.customer.dao.entity.CarGroupPeriod;
import cn.suparking.customer.dao.entity.CarGroupStockDO;
import cn.suparking.customer.dao.mapper.CarGroupMapper;
import cn.suparking.customer.dao.mapper.CarGroupPeriodMapper;
import cn.suparking.customer.dao.mapper.CarGroupStockMapper;
import cn.suparking.customer.dao.vo.cargroup.MyVipCarVo;
import cn.suparking.customer.dao.vo.cargroup.ProjectVipCarVo;
import cn.suparking.customer.dao.vo.cargroup.ProtocolVipCarVo;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Service
public class MyVipCarServiceImpl implements MyVipCarService {

    private final CarGroupMapper carGroupMapper;

    private final CarGroupPeriodMapper carGroupPeriodMapper;

    private final CarGroupStockMapper carGroupStockMapper;

    @Resource
    private SparkProperties sparkProperties;

    public MyVipCarServiceImpl(final CarGroupMapper carGroupMapper, final CarGroupPeriodMapper carGroupPeriodMapper, final CarGroupStockMapper carGroupStockMapper) {
        this.carGroupMapper = carGroupMapper;
        this.carGroupPeriodMapper = carGroupPeriodMapper;
        this.carGroupStockMapper = carGroupStockMapper;
    }

    /**
     * 获取当前用户所有场库所办的合约信息.
     *
     * @param vipCarQueryDTO {@linkplain VipCarQueryDTO}
     * @return {@link SpkCommonResult}
     * @author ZDD
     * @date 2022/7/20 14:53:11
     */
    @Override
    public List<MyVipCarVo> myVipCarList(final VipCarQueryDTO vipCarQueryDTO) {
        List<MyVipCarVo> list = new ArrayList<>();

        //根据用户id查询用户合约
        List<CarGroup> carGroupList = carGroupMapper.findByUserId(Long.valueOf(vipCarQueryDTO.getUserId()));

        //用户不存在合约
        if (Objects.isNull(carGroupList)) {
            return list;
        }

        for (CarGroup carGroup : carGroupList) {
            //根据合约协议id获取协议信息
            String protocolId = carGroup.getProtocolId();
            Map<String, Object> params = new HashMap<>();
            params.put("protocolId", protocolId);

            JSONObject result = HttpRequestUtils.sendGet(sparkProperties.getUrl() + ParkConstant.INTERFACE_MYVIPCARINFO, params);
            if (Objects.isNull(result) || !ParkConstant.SUCCESS.equals(result.getString("code"))) {
                log.warn("获取用户所办合约列表失败 <====== 协议id [{}] 无对应协议, 属于数据异常", protocolId);
                continue;
            }
            JSONObject protocol = result.getJSONObject("protocol");

            //判断是否可以线上续费
            JSONArray userServicesArr = protocol.getJSONArray("userServices");
            List<String> userServices = JSONArray.parseArray(JSONObject.toJSONString(userServicesArr), String.class);

            Boolean canRenew = false;
            if (userServices.contains("RENEW")) {
                canRenew = true;
            }

            //生效中的租期时间 如果没有则返回null 表示已过期
            List<CarGroupPeriod> carGroupPeriods = carGroupPeriodMapper.findByCarGroupId(carGroup.getId());
            CarGroupPeriod period = getEffectPeriod(carGroupPeriods);

            //获取待生效时间段
            List<CarGroupPeriod> futureList = getFutureList(carGroupPeriods);

            JSONObject project = result.getJSONObject("project");
            MyVipCarVo myVipCarVo = MyVipCarVo.builder().id(String.valueOf(carGroup.getId())).userId(vipCarQueryDTO.getUserId())
                    .projectNo(carGroup.getProjectNo()).projectName(project.getString("projectName"))
                    .carTypeName(carGroup.getCarTypeName()).protocolId(protocolId)
                    .protocolName(carGroup.getProtocolName()).protocolDesc(protocol.getString("protocolDesc"))
                    .canRenew(canRenew).futureList(futureList).build();

            if (period != null) {
                myVipCarVo.setEndDate(period.getEndDate());
            }
            list.add(myVipCarVo);
        }
        return list;
    }

    /**
     * 获取线上所有可办合约场库列表.
     *
     * @return {@link SpkCommonResult}
     * @author ZDD
     * @date 2022/7/20 14:53:11
     */
    @Override
    public List<ProjectVipCarVo> projectVipCarList() {
        List<ProjectVipCarVo> projectVipCarVoList = new ArrayList<>();

        //获取所有可线上办理的合约协议
        JSONObject protocolListResult = HttpRequestUtils.sendGet(sparkProperties.getUrl() + ParkConstant.INTERFACE_NEWPROTOCOL, new HashMap<>());
        if (Objects.isNull(protocolListResult) || !ParkConstant.SUCCESS.equals(protocolListResult.getString("code"))) {
            log.warn("获取协议列表失败");
            throw new SpkCommonException("获取场库列表失败");
        }

        JSONArray protocolList = protocolListResult.getJSONArray("protocolList");
        if (Objects.isNull(protocolList)) {
            return projectVipCarVoList;
        }

        //取涵盖的场库去重
        Set<String> projectNoList = new HashSet<>();
        for (int i = 0; i < protocolList.size(); i++) {
            JSONObject protocol = protocolList.getJSONObject(i);
            String projectNo = protocol.getString("projectNo");
            projectNoList.add(projectNo);
        }

        //查询场库信息
        Map<String, Object> params = new HashMap<>();
        params.put("projectNoList", new ArrayList<>(projectNoList));
        JSONObject resultJSON = HttpRequestUtils.sendPost(sparkProperties.getUrl() + ParkConstant.INTERFACE_PROJECTLIST, params);
        if (Objects.isNull(resultJSON) || !ParkConstant.SUCCESS.equals(resultJSON.getString("code"))) {
            log.warn("获取场库列表失败");
            throw new SpkCommonException("获取场库列表失败");
        }

        JSONArray projectList = resultJSON.getJSONArray("projectList");
        if (Objects.isNull(projectList)) {
            return projectVipCarVoList;
        }

        for (int i = 0; i < projectList.size(); i++) {
            JSONObject project = projectList.getJSONObject(i);
            ProjectVipCarVo projectVipCarVo = ProjectVipCarVo.builder().id(project.getString("id"))
                    .projectNo(project.getString("projectNo"))
                    .projectName(project.getString("projectName"))
                    .status(true)
                    .build();

            JSONObject location = project.getJSONObject("location");
            if (Objects.nonNull(location)) {
                projectVipCarVo.setLongitude(location.getBigDecimal("longitude"));
                projectVipCarVo.setLatitude(location.getBigDecimal("latitude"));
            }
            if (Objects.nonNull(project.getJSONArray("openTime"))) {
                List<String> openTime = JSONArray.parseArray(JSONObject.toJSONString(project.getJSONArray("openTime")), String.class);
                projectVipCarVo.setOpenTime(openTime);
                projectVipCarVo.setStatus(openState(openTime));
            }
            projectVipCarVoList.add(projectVipCarVo);
        }
        return projectVipCarVoList;
    }

    /**
     * 获取线上所有可办合约列表.
     *
     * @param projectNo 场库编号
     * @return {@link ProtocolVipCarVo}
     * @author ZDD
     * @date 2022/7/20 14:53:11
     */
    @Override
    public List<ProtocolVipCarVo> protocolVipCarList(final String projectNo) {
        List<ProtocolVipCarVo> protocolVipCarVoList = new ArrayList<>();

        //获取指定场库可线上办理的合约协议
        Map<String, Object> params = new HashMap<>();
        params.put("projectNo", projectNo);
        JSONObject protocolListResult = HttpRequestUtils.sendGet(sparkProperties.getUrl() + ParkConstant.INTERFACE_NEWPROTOCOLBYPROJECTNO, params);
        if (Objects.isNull(protocolListResult) || !ParkConstant.SUCCESS.equals(protocolListResult.getString("code"))) {
            log.warn("获取协议列表失败");
            throw new SpkCommonException("获取协议列表失败");
        }

        JSONArray protocolList = protocolListResult.getJSONArray("protocolList");
        if (Objects.isNull(protocolList)) {
            return protocolVipCarVoList;
        }

        for (int i = 0; i < protocolList.size(); i++) {
            JSONObject protocol = protocolList.getJSONObject(i);
            ProtocolVipCarVo protocolVipCarVo = ProtocolVipCarVo.builder().id(protocol.getString("id"))
                    .projectNo(protocol.getString("projectNo"))
                    .carTypeName(protocol.getString("carTypeName"))
                    .protocolId(protocol.getString("id"))
                    .protocolName(protocol.getString("protocolName"))
                    .protocolDesc(protocol.getString("protocolDesc"))
                    .price(protocol.getInteger("price"))
                    .stockQuantity(0)
                    .build();

            //查询合约库存
            CarGroupStockDO carGroupStock = carGroupStockMapper.findByProtocolId(protocol.getString("id"));
            if (Objects.nonNull(carGroupStock)) {
                protocolVipCarVo.setStockQuantity(carGroupStock.getStockQuantity());
            }

            JSONObject duration = protocol.getJSONObject("duration");
            if (Objects.nonNull(duration)) {
                protocolVipCarVo.setDurationType(duration.getString("durationType"));
                protocolVipCarVo.setQuantity(duration.getInteger("quantity"));
            }

            protocolVipCarVoList.add(protocolVipCarVo);
        }
        return protocolVipCarVoList;
    }

    /**
     * 根据当前时间 获取合约的开始日期和结束日期.
     *
     * @param carGroupPeriodList {@linkplain CarGroupPeriod}
     * @return {@link CarGroupPeriod}
     * @author ZDD
     * @date 2022/7/20 16:58:25
     */
    private CarGroupPeriod getEffectPeriod(final List<CarGroupPeriod> carGroupPeriodList) {
        Long nowTime = DateUtil.currentSeconds();
        //排序 ======> 从小到大
        Collections.sort(carGroupPeriodList);
        for (int i = 0; i < carGroupPeriodList.size(); i++) {
            CarGroupPeriod period = carGroupPeriodList.get(i);
            Long beginDate = period.getBeginDate();
            Long endDate = period.getEndDate();
            //生效中
            if (nowTime >= beginDate && nowTime <= endDate) {
                //表示 ======> 合约处于生效中
                return period;
            }
        }
        return null;
    }

    /**
     * 私有方法 ======> 根据当前时间 获取合约租期中的待生效的时间段.
     *
     * @param carGroupPeriodList {@linkplain CarGroupPeriod}
     * @return {@link CarGroupPeriod}
     * @author ZDD
     * @date 2022/7/20 16:58:25
     */
    private List<CarGroupPeriod> getFutureList(final List<CarGroupPeriod> carGroupPeriodList) {
        List<CarGroupPeriod> list = new ArrayList<>();
        Long nowTime = DateUtil.currentSeconds();
        //排序 ======> 从小到大
        Collections.sort(carGroupPeriodList);
        for (int i = 0; i < carGroupPeriodList.size(); i++) {
            CarGroupPeriod period = carGroupPeriodList.get(i);
            Long beginDate = period.getBeginDate();
            if (beginDate > nowTime) {
                list.add(period);
            }
        }
        return list;
    }

    /**
     * 开放状态.
     *
     * @param openTime 开放时间
     * @return boolean
     * @author ZDD
     * @date 2022/7/20 20:38:07
     */
    private boolean openState(final List<String> openTime) {
        if (CollectionUtils.isEmpty(openTime) || openTime.size() < 2) {
            return false;
        }

        //当前距离凌晨的秒值
        Long currentMillis = 0L;
        long now = System.currentTimeMillis();
        SimpleDateFormat sdfOne = new SimpleDateFormat("yyyy-MM-dd");
        try {
            currentMillis = (now - (sdfOne.parse(sdfOne.format(now)).getTime())) / 1000;
        } catch (ParseException e) {
            log.warn("获取时间数据异常 [{}]", e.getMessage());
            e.printStackTrace();
        }

        String startTime = openTime.get(0);
        String endTime = openTime.get(1);

        //开始时间距离凌晨秒值
        Long startSecond = 0L;
        startSecond = startSecond + Integer.valueOf(startTime.split(":")[0]) * 60 * 60;
        startSecond = startSecond + Integer.valueOf(startTime.split(":")[1]) * 60;

        //结束时间距离凌晨秒值
        Long endSecond = 0L;
        endSecond = endSecond + Integer.valueOf(endTime.split(":")[0]) * 60 * 60;
        endSecond = endSecond + Integer.valueOf(endTime.split(":")[1]) * 60;

        //跨天处理
        if (startSecond > endSecond) {
            return currentMillis <= startSecond || currentMillis >= endSecond;
        } else {
            return currentMillis >= startSecond && currentMillis <= endSecond;
        }
    }
}
