package cn.suparking.customer.controller.park.service.impl;

import cn.suparking.common.tools.HttpUtils;
import cn.suparking.customer.api.constant.ParkConstant;
import cn.suparking.customer.beans.park.LocationDTO;
import cn.suparking.customer.configuration.properties.SparkProperties;
import cn.suparking.customer.controller.park.service.ParkService;
import cn.suparking.customer.vo.park.ParkInfoVO;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
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
        JSONObject requestParam = new JSONObject();
        requestParam.put("latitude", locationDTO.getLatitude());
        requestParam.put("longitude", locationDTO.getLongitude());
        requestParam.put("number", locationDTO.getNumber());
        requestParam.put("radius", locationDTO.getRadius());
        JSONObject result = HttpUtils.sendPost(sparkProperties.getUrl() + ParkConstant.INTERFACE_NEARBYPARK, requestParam.toJSONString());
        List<ParkInfoVO> parkInfoVOList = new LinkedList<>();
        return getParkInfoVOS(parkInfoVOList, result);
    }

    @Override
    public List<ParkInfoVO> allLocation() {
        JSONObject result = HttpUtils.sendGet(sparkProperties.getUrl() + ParkConstant.INTERFACE_ALLPARK, null);
        List<ParkInfoVO> parkInfoVOList = new LinkedList<>();
        return getParkInfoVOS(parkInfoVOList, result);
    }

    private List<ParkInfoVO> getParkInfoVOS(final List<ParkInfoVO> parkInfoVOList, final JSONObject result) {
        return Optional.ofNullable(result).filter(res -> SUCCESS.equals(res.getString("code"))).map(item -> {
            JSONArray jsonArray = item.getJSONArray("list");
            jsonArray.forEach(obj -> {
                try {
                    parkInfoVOList.add(JSON.toJavaObject((JSONObject) obj, ParkInfoVO.class));
                } catch (Exception e) {
                    Arrays.stream(e.getStackTrace()).forEach(err -> log.error(err.toString()));
                }
            });
            return parkInfoVOList;
        }).orElse(null);
    }
}
