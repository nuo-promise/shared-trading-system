package cn.suparking.customer.controller.park.service;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.customer.api.beans.ParkFeeQueryDTO;
import cn.suparking.customer.api.beans.ParkPayDTO;
import cn.suparking.customer.api.beans.ProjectInfoQueryDTO;
import cn.suparking.customer.api.beans.ProjectQueryDTO;
import cn.suparking.customer.api.beans.discount.DiscountDTO;
import cn.suparking.customer.api.beans.order.OrderDTO;
import cn.suparking.customer.beans.park.LocationDTO;
import cn.suparking.customer.beans.park.RegularLocationDTO;
import cn.suparking.customer.vo.park.ParkInfoVO;

import java.util.List;
import java.util.Map;

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

    /**
     * 向B端 根据用户ID获取常去的场库.
     * @return {@link Map}
     */
    Map<String, ParkInfoVO> allLocationMap();

    /**
     * 查询设备计费信息.
     * @param sign sign
     * @param parkFeeQueryDTO {@link ParkFeeQueryDTO}
     * @return {@link SpkCommonResult}
     */
    SpkCommonResult scanCodeQueryFee(String sign, ParkFeeQueryDTO parkFeeQueryDTO);

    /**
     * 小程序下单接口.
     * @param sign C 端 下单签名
     * @param parkPayDTO {@link ParkPayDTO}
     * @return {@link SpkCommonResult}
     */
    SpkCommonResult miniToPay(String sign, ParkPayDTO parkPayDTO);

    /**
     * 订单查询接口.
     * @param sign C 端 下单签名
     * @param orderDTO {@link OrderDTO}
     * @return {@link SpkCommonResult}
     */
    SpkCommonResult queryOrder(String sign, OrderDTO orderDTO);

    /**
     * 订单关闭接口.
     * @param sign C 端 下单签名
     * @param orderDTO {@link OrderDTO}
     * @return {@link SpkCommonResult}
     */
    SpkCommonResult closeOrder(String sign, OrderDTO orderDTO);

    /**
     * 清除费用缓存记录.
     * @param sign C 端 下单签名
     * @param parkPayDTO {@link ParkPayDTO}
     * @return {@link SpkCommonResult}
     */
    SpkCommonResult clearParkCache(String sign, ParkPayDTO parkPayDTO);

    /**
     * 清除优惠券缓存.
     * @param sign C 端 下单签名
     * @param discountDTO {@link DiscountDTO}
     * @return {@link SpkCommonResult}
     */
    SpkCommonResult clearDiscountCache(String sign, DiscountDTO discountDTO);

    /**
     * 根据优惠券编号获取优惠券信息.
     * @param sign C 端 下单签名
     * @param discountDTO {@link DiscountDTO}
     * @return {@link SpkCommonResult}
     */
    SpkCommonResult discountInfoByScanCode(String sign, DiscountDTO discountDTO);

    /**
     * 根据用户ID获取常去的场库.
     * @param regularLocationDTO {@link RegularLocationDTO}
     * @return {@link SpkCommonResult}
     */
    SpkCommonResult regularByPark(RegularLocationDTO regularLocationDTO);

    /**
     * 根据设备编号查询项目信息.
     * @param sign C 端 使用 deviceNo 进行签名制作.
     * @param projectQueryDTO {@link ProjectQueryDTO}
     * @return {@link SpkCommonResult}
     */
    SpkCommonResult projectInfoByDeviceNo(String sign, ProjectQueryDTO projectQueryDTO);

    /**
     * 根据项目号查询项目信息.
     * @param sign C 端 使用 deviceNo 进行签名制作.
     * @param projectInfoQueryDTO {@link ProjectInfoQueryDTO}
     * @return {@link SpkCommonResult}
     */
    SpkCommonResult projectInfoByProjectNo(String sign, ProjectInfoQueryDTO projectInfoQueryDTO);

    /**
     * 根据项目和车位编号查询设备信息.
     * @param sign C 端 使用 deviceNo 进行签名制作.
     * @param projectInfoQueryDTO {@link ProjectInfoQueryDTO}
     * @return {@link SpkCommonResult}
     */
    SpkCommonResult getDeviceNo(String sign, ProjectInfoQueryDTO projectInfoQueryDTO);

    /**
     * 根据UnionId 获取未使用优惠券.
     * @param sign C 端 使用 deviceNo 进行签名制作.
     * @param unionId {@link String}
     * @return {@link SpkCommonResult}
     */
    SpkCommonResult getDiscountInfoCount(String sign, String unionId);

    /**
     * 根据UnionId 获取未使用优惠券.
     * @param sign C 端 使用 deviceNo 进行签名制作.
     * @param unionId {@link String}
     * @return {@link SpkCommonResult}
     */
    SpkCommonResult getDiscountInfo(String sign, String unionId);
}
