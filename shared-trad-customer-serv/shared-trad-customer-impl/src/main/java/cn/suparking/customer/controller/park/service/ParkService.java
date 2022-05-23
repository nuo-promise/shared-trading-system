package cn.suparking.customer.controller.park.service;

import cn.suparking.customer.beans.park.LocationDTO;
import cn.suparking.customer.vo.park.ParkInfoVO;

import java.util.List;

public interface ParkService {

    /**
     * 向B端 根据当前经纬度 获取最近的场库信息.
     * @param locationDTO {@link LocationDTO}
     * @return {@link List}
     */
    List<ParkInfoVO> nearByPark(LocationDTO locationDTO);

    /**
     * 向B端 查询所有场库列表.
     * @return {@link List}
     */
    List<ParkInfoVO> allLocation();
}
