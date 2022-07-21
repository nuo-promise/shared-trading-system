package cn.suparking.customer.controller.cargroup.service.impl;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.customer.api.beans.cargroup.CarGroupDTO;
import cn.suparking.customer.api.beans.cargroup.CarGroupQueryDTO;
import cn.suparking.customer.dao.entity.CarGroup;
import cn.suparking.customer.dao.entity.CarGroupPeriod;
import cn.suparking.customer.dao.vo.cargroup.CarGroupVO;
import cn.suparking.customer.controller.cargroup.service.CarGroupService;
import cn.suparking.customer.dao.mapper.CarGroupMapper;
import cn.suparking.customer.dao.mapper.CarGroupPeriodMapper;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class CarGroupServiceImpl implements CarGroupService {

    private final CarGroupMapper carGroupMapper;

    private final CarGroupPeriodMapper carGroupPeriodMapper;

    public CarGroupServiceImpl(final CarGroupMapper carGroupMapper, final CarGroupPeriodMapper carGroupPeriodMapper) {
        this.carGroupMapper = carGroupMapper;
        this.carGroupPeriodMapper = carGroupPeriodMapper;
    }

    /**
     * 合约列表.
     *
     * @param carGroupQueryDTO {@link CarGroupQueryDTO}
     * @return {@link List}
     */
    @Override
    public PageInfo<CarGroupVO> list(final CarGroupQueryDTO carGroupQueryDTO) {
        log.info("用户 [{}] 请求获取合约列表,请求参数 -> {}", carGroupQueryDTO.getLoginUserName(), JSONObject.toJSONString(carGroupQueryDTO));
        PageHelper.startPage(carGroupQueryDTO.getPageNum(), carGroupQueryDTO.getPageSize());
        //求总数
        long total = carGroupMapper.listTotal(carGroupQueryDTO);
        List<CarGroupVO> carGroupList = carGroupMapper.list(carGroupQueryDTO);
        PageInfo<CarGroupVO> carGroupVOPageInfo = new PageInfo<>(carGroupList);
        carGroupVOPageInfo.setTotal(total);
        return carGroupVOPageInfo;
    }

    /**
     * 合约添加.
     *
     * @param carGroupDTO {@link CarGroupDTO}
     * @return {@link List}
     */
    @Override
    @Transactional
    public SpkCommonResult insert(final CarGroupDTO carGroupDTO) {
        CarGroup carGroup = CarGroup.buildCarGroup(carGroupDTO);
        CarGroupVO carGroupVO = CarGroupVO.builder().build();
        int insert = carGroupMapper.insert(carGroup);
        if (insert > 0) {
            carGroupDTO.setId(carGroup.getId());
            CarGroupPeriod carGroupPeriod = CarGroupPeriod.buildCarGroup(carGroupDTO);
            carGroupPeriodMapper.insert(carGroupPeriod);
            BeanUtils.copyProperties(carGroup, carGroupVO);
            carGroupVO.setCarGroupPeriodList(Arrays.asList(carGroupPeriod));
            return SpkCommonResult.success(carGroupVO);
        }
        return SpkCommonResult.error("合约创建失败");
    }

    /**
     * 合约删除.
     *
     * @param carGroupDTO {@link CarGroupDTO}
     * @return {@link List}
     */
    @Override
    @Transactional
    public SpkCommonResult remove(final CarGroupDTO carGroupDTO) {
        int delete = carGroupMapper.deleteById(carGroupDTO.getId());
        if (delete < 1) {
            return SpkCommonResult.error("合约删除失败");
        }
        carGroupPeriodMapper.deleteByCarGroupId(carGroupDTO.getId());
        return SpkCommonResult.success();
    }

    /**
     * 根据id查询合约信息.
     *
     * @param carGroupDTO {@link CarGroupDTO}
     * @return {@link List}
     */
    @Override
    public SpkCommonResult findById(final CarGroupDTO carGroupDTO) {
        CarGroupVO carGroupVO = carGroupMapper.findById(carGroupDTO.getId());
        if (ObjectUtils.isEmpty(carGroupVO)) {
            return SpkCommonResult.error("对应合约不存在");
        }
        return SpkCommonResult.success(carGroupVO);
    }
}
