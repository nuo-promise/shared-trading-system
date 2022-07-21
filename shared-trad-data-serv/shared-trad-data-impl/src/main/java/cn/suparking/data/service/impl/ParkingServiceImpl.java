package cn.suparking.data.service.impl;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.data.api.beans.ParkingDTO;
import cn.suparking.data.api.query.ParkingQueryDTO;
import cn.suparking.data.dao.entity.ParkingDO;
import cn.suparking.data.dao.mapper.ParkingMapper;
import cn.suparking.data.feign.order.UserTemplateService;
import cn.suparking.data.service.ParkingService;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ParkingServiceImpl implements ParkingService {

    private final ParkingMapper parkingMapper;

    private final UserTemplateService userTemplateService;

    public ParkingServiceImpl(final ParkingMapper parkingMapper, final UserTemplateService userTemplateService) {
        this.parkingMapper = parkingMapper;
        this.userTemplateService = userTemplateService;
    }

    @Override
    public ParkingDO findById(final Long id) {
        return parkingMapper.selectById(id);
    }

    @Override
    public Integer createOrUpdate(final ParkingDTO parkingDTO) {
        ParkingDO parkingDO = ParkingDO.buildParkingDO(parkingDTO);
        if (StringUtils.isEmpty(parkingDTO.getId())) {
            return parkingMapper.insert(parkingDO);
        }
        return parkingMapper.update(parkingDO);
    }

    @Override
    public ParkingDO findByProjectIdAndParkId(final Map<String, Object> params) {
        return parkingMapper.findByProjectIdAndParkId(params);
    }

    /**
     * 根据条件获取停车记录.
     *
     * @param parkingQueryDTO String
     * @return {@link ParkingQueryDTO}
     */
    @Override
    public SpkCommonResult list(final ParkingQueryDTO parkingQueryDTO) {
        //如果手机号不为空，先根据手机号查询user表，获取userId
        String iphone = parkingQueryDTO.getKeyword();
        if (!StringUtils.isBlank(iphone)) {
            JSONObject userByIphone = userTemplateService.getUserByIphone(iphone);
            if (userByIphone == null || userByIphone.getInteger("code") != 200 || ObjectUtils.isEmpty(userByIphone.getJSONObject("data"))) {
                return SpkCommonResult.success();
            }
            JSONObject data = userByIphone.getJSONObject("data");
            parkingQueryDTO.setUserId(data.getLong("id"));
        }

        String parkingState = parkingQueryDTO.getParkingState();
        if (!StringUtils.isBlank(parkingState)) {
            if (parkingState.equals("enter")) {
                parkingQueryDTO.setParkingStateList(Arrays.asList("ENTERED", "ENTERED_FORCE"));
            }
            if (parkingState.equals("leave")) {
                parkingQueryDTO.setParkingStateList(Arrays.asList("LEFT", "LEFT_FORCE", "LEFT_ABNORMAL"));
            }
            if (parkingState.equals("parking")) {
                parkingQueryDTO.setParkingStateList(Arrays.asList("LEFT", "LEFT_FORCE", "LEFT_ABNORMAL", "ENTERED", "ENTERED_FORCE", "PASS_WAIT", "PAY_WAIT", "LEAVE_UNMATCHED", "PASS_UNMATCHED"));
            }
        }

        PageHelper.startPage(parkingQueryDTO.getPage(), parkingQueryDTO.getSize());
        List<ParkingDO> parkingDOList = parkingMapper.list(parkingQueryDTO);
        PageInfo<ParkingDO> carGroupVOPageInfo = new PageInfo<>(parkingDOList);
        return SpkCommonResult.success(carGroupVOPageInfo);
    }

    @Override
    public ParkingDO findByPayParkingId(String payParkingId) {
        return parkingMapper.selectByPayParkingId(payParkingId);
    }
}
