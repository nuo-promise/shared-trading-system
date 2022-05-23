package cn.suparking.customer.controller.park.service.impl;

import cn.suparking.common.tools.HttpUtils;
import cn.suparking.customer.api.constant.ParkConstant;
import cn.suparking.customer.beans.park.LocationDTO;
import cn.suparking.customer.configuration.properties.SparkProperties;
import cn.suparking.customer.controller.park.service.ParkService;
import cn.suparking.customer.vo.park.ParkInfoVO;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static cn.suparking.customer.api.constant.ParkConstant.SUCCESS;

@Slf4j
@Service
public class ParkServiceImpl implements ParkService {

    @Resource
    private SparkProperties sparkProperties;

    @Override
    public List<ParkInfoVO> nearByPark(final LocationDTO locationDTO) {
        List<ParkInfoVO> parkInfoVOList = new LinkedList<>();
        JSONObject requestParam = new JSONObject();
        requestParam.put("latitude", locationDTO.getLatitude());
        requestParam.put("longitude", locationDTO.getLongitude());
        requestParam.put("number", locationDTO.getNumber());
        JSONObject result = HttpUtils.sendPost(sparkProperties.getUrl() + ParkConstant.INTERFACE_NEARBYPARK, requestParam.toJSONString());
        return Optional.ofNullable(result).map(item -> {
            if (SUCCESS.equals(item.getString("code"))) {
                JSONArray listArray = item.getJSONArray("list");
                listArray.forEach(listItem -> {
                    parkInfoVOList.add()
                })

            }
        }).orElse(null);
    }

    @Override
    public List<ParkInfoVO> allLocation() {
        JSONObject result = HttpUtils.sendGet(sparkProperties.getUrl() + ParkConstant.INTERFACE_ALLPARK, null);
        return Optional.ofNullable(result).map(item -> {
            return new LinkedList();
        }).orElse(null);
    }
}
