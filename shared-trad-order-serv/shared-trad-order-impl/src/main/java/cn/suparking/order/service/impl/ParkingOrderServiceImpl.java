package cn.suparking.order.service.impl;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.data.api.parkfee.ChargeDetail;
import cn.suparking.data.api.parkfee.ChargeInfo;
import cn.suparking.data.api.parkfee.DiscountInfo;
import cn.suparking.data.api.parkfee.ParkingOrder;
import cn.suparking.order.api.beans.ChargeDetailDTO;
import cn.suparking.order.api.beans.ChargeInfoDTO;
import cn.suparking.order.api.beans.DiscountInfoDTO;
import cn.suparking.order.api.beans.OrderDTO;
import cn.suparking.order.api.beans.ParkingOrderDTO;
import cn.suparking.order.api.beans.ParkingOrderQueryDTO;
import cn.suparking.order.api.beans.ParkingQuery;
import cn.suparking.order.dao.convert.ParkOrderToLockOrderVO;
import cn.suparking.order.dao.entity.ChargeDetailDO;
import cn.suparking.order.dao.entity.ChargeInfoDO;
import cn.suparking.order.dao.entity.DiscountInfoDO;
import cn.suparking.order.dao.entity.ParkingOrderDO;
import cn.suparking.order.dao.mapper.ChargeDetailMapper;
import cn.suparking.order.dao.mapper.ChargeInfoMapper;
import cn.suparking.order.dao.mapper.DiscountInfoMapper;
import cn.suparking.order.dao.mapper.ParkingOrderMapper;
import cn.suparking.order.dao.vo.ChargeInfoVO;
import cn.suparking.order.dao.vo.LockOrderVO;
import cn.suparking.order.dao.vo.ParkingOrderVO;
import cn.suparking.order.feign.order.UserTemplateService;
import cn.suparking.order.service.ChargeDetailService;
import cn.suparking.order.service.ChargeInfoService;
import cn.suparking.order.service.DiscountInfoService;
import cn.suparking.order.service.ParkingOrderService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class ParkingOrderServiceImpl implements ParkingOrderService {

    private final ChargeDetailMapper chargeDetailMapper;

    private final DiscountInfoMapper discountInfoMapper;

    private final ChargeInfoMapper chargeInfoMapper;

    private final ParkingOrderMapper parkingOrderMapper;

    private final UserTemplateService userTemplateService;

    private final ChargeInfoService chargeInfoService;

    private final ChargeDetailService chargeDetailService;

    private final DiscountInfoService discountInfoService;

    public ParkingOrderServiceImpl(final ChargeDetailMapper chargeDetailMapper, final DiscountInfoMapper discountInfoMapper,
                                   final ChargeInfoMapper chargeInfoMapper, final UserTemplateService userTemplateService,
                                   final ParkingOrderMapper parkingOrderMapper, final ChargeInfoService chargeInfoService,
                                   final ChargeDetailService chargeDetailService, final DiscountInfoService discountInfoService) {
        this.chargeDetailMapper = chargeDetailMapper;
        this.discountInfoMapper = discountInfoMapper;
        this.chargeInfoMapper = chargeInfoMapper;
        this.userTemplateService = userTemplateService;
        this.parkingOrderMapper = parkingOrderMapper;
        this.chargeInfoService = chargeInfoService;
        this.chargeDetailService = chargeDetailService;
        this.discountInfoService = discountInfoService;
    }

    @Override
    public ParkingOrderDO findById(final String id) {
        return parkingOrderMapper.selectById(id);
    }

    @Override
    public Long createOrUpdate(final ParkingOrderDTO parkingOrderDTO) {
        ParkingOrderDO parkingOrderDO = ParkingOrderDO.buildParkingOrderDO(parkingOrderDTO);
        if (StringUtils.isEmpty(parkingOrderDTO.getId())) {
            if (parkingOrderMapper.insert(parkingOrderDO) == 1) {
                return parkingOrderDO.getId();
            } else {
                return -1L;
            }
        } else {
            parkingOrderMapper.update(parkingOrderDO);
        }
        return parkingOrderDO.getId();
    }

    /**
     * 根据userId查询常去车场.
     *
     * @param userId 用户id
     * @param count  查询记录数
     * @return {@linkplain SpkCommonResult}
     */
    @Override
    public List<String> regularLocations(final Long userId, final Integer count) {
        PageHelper.startPage(1, count == null ? 5 : count);
        List<String> projectNoList = parkingOrderMapper.detailParkingOrder(userId);
        PageInfo<String> carGroupOrderDOPageInfo = new PageInfo<>(projectNoList);
        return carGroupOrderDOPageInfo.getList();
    }

    @Override
    public SpkCommonResult findByUserIdsAndBeginTimeOrEndTimeRange(final ParkingQuery parkingQuery) {
        Map<String, Object> params = new HashMap<>();
        params.put("projectNo", parkingQuery.getProjectNo());
        params.put("userIds", parkingQuery.getUserIds());
        params.put("begin", parkingQuery.getBegin());
        params.put("end", parkingQuery.getEnd());
        // 获取 ParkingOrderDO + ChargeInfo + DiscountInfo 信息
        List<ParkingOrderDO> parkingOrderDOList = parkingOrderMapper.findByUserIdsAndBeginTimeOrEndTimeRange(params);
        if (Objects.nonNull(parkingOrderDOList) && parkingOrderDOList.isEmpty()) {
            return SpkCommonResult.error("无订单信息");
        }
        List<ParkingOrderVO> parkingOrderVOList = new ArrayList<>(parkingOrderDOList.size());
        parkingOrderDOList.forEach(parkingOrderDO -> {
            ParkingOrderVO parkingOrderVO = new ParkingOrderVO();
            BeanUtils.copyProperties(parkingOrderDO, parkingOrderVO);
            DiscountInfoDO discountInfoDO = discountInfoMapper.findByParkingOrderId(parkingOrderDO.getId());
            parkingOrderVO.setDiscountInfoDO(discountInfoDO);
            List<ChargeInfoDO> chargeInfoDOList = chargeInfoMapper.findByParkingOrderId(parkingOrderDO.getId());
            LinkedList<ChargeInfoVO> chargeInfoVOList = new LinkedList<>();
            if (Objects.nonNull(chargeInfoDOList) && !chargeInfoDOList.isEmpty()) {
                chargeInfoDOList.forEach(chargeInfo -> {
                    ChargeInfoVO chargeInfoVO = new ChargeInfoVO();
                    BeanUtils.copyProperties(chargeInfo, chargeInfoVO);
                    LinkedList<ChargeDetailDO> chargeDetailDOList = chargeDetailMapper.findByChargeInfoId(chargeInfo.getId());
                    if (Objects.nonNull(chargeDetailDOList) && !chargeDetailDOList.isEmpty()) {
                        Collections.sort(chargeDetailDOList);
                        chargeInfoVO.setChargeDetailDOList(chargeDetailDOList);
                    }
                    chargeInfoVOList.add(chargeInfoVO);
                });
            }
            parkingOrderVO.setChargeInfos(chargeInfoVOList);
            parkingOrderVOList.add(parkingOrderVO);
        });

        return SpkCommonResult.success(parkingOrderVOList);
    }

    @Override
    public SpkCommonResult findByUserIdsAndEndTimeRange(final ParkingQuery parkingQuery) {
        Map<String, Object> params = new HashMap<>();
        params.put("projectNo", parkingQuery.getProjectNo());
        params.put("userIds", parkingQuery.getUserIds());
        params.put("begin", parkingQuery.getBegin());
        params.put("end", parkingQuery.getEnd());
        // 获取 ParkingOrderDO + ChargeInfo + DiscountInfo 信息
        List<ParkingOrderDO> parkingOrderDOList = parkingOrderMapper.findByUserIdsAndBeginTimeOrEndTimeRange(params);
        if (Objects.nonNull(parkingOrderDOList) && parkingOrderDOList.isEmpty()) {
            return SpkCommonResult.error("无订单信息");
        }
        List<ParkingOrderVO> parkingOrderVOList = new ArrayList<>(parkingOrderDOList.size());
        parkingOrderDOList.forEach(parkingOrderDO -> {
            ParkingOrderVO parkingOrderVO = new ParkingOrderVO();
            BeanUtils.copyProperties(parkingOrderDO, parkingOrderVO);
            DiscountInfoDO discountInfoDO = discountInfoMapper.findByParkingOrderId(parkingOrderDO.getId());
            parkingOrderVO.setDiscountInfoDO(discountInfoDO);
            List<ChargeInfoDO> chargeInfoDOList = chargeInfoMapper.findByParkingOrderId(parkingOrderDO.getId());
            LinkedList<ChargeInfoVO> chargeInfoVOList = new LinkedList<>();
            if (Objects.nonNull(chargeInfoDOList) && !chargeInfoDOList.isEmpty()) {
                chargeInfoDOList.forEach(chargeInfo -> {
                    ChargeInfoVO chargeInfoVO = new ChargeInfoVO();
                    BeanUtils.copyProperties(chargeInfo, chargeInfoVO);
                    LinkedList<ChargeDetailDO> chargeDetailDOList = chargeDetailMapper.findByChargeInfoId(chargeInfo.getId());
                    if (Objects.nonNull(chargeDetailDOList) && !chargeDetailDOList.isEmpty()) {
                        Collections.sort(chargeDetailDOList);
                        chargeInfoVO.setChargeDetailDOList(chargeDetailDOList);
                    }
                    chargeInfoVOList.add(chargeInfoVO);
                });
            }
            parkingOrderVO.setChargeInfos(chargeInfoVOList);
            parkingOrderVOList.add(parkingOrderVO);
        });

        return SpkCommonResult.success(parkingOrderVOList);
    }

    @Override
    public SpkCommonResult findNextAggregateBeginTime(final ParkingQuery parkingQuery) {
        Map<String, Object> params = new HashMap<>();
        params.put("projectNo", parkingQuery.getProjectNo());
        params.put("userIds", parkingQuery.getUserIds());

        ParkingOrderDO parkingOrderDO = parkingOrderMapper.findNextAggregateBeginTime(params);
        if (Objects.nonNull(parkingOrderDO)) {
            return SpkCommonResult.error("无订单信息");
        }
        List<ChargeInfoDO> chargeInfoDOList = chargeInfoMapper.findByParkingOrderId(parkingOrderDO.getId());
        LinkedList<ChargeInfoVO> chargeInfoVOList = new LinkedList<>();
        if (Objects.nonNull(chargeInfoDOList) && !chargeInfoDOList.isEmpty()) {
            chargeInfoDOList.forEach(chargeInfo -> {
                ChargeInfoVO chargeInfoVO = new ChargeInfoVO();
                BeanUtils.copyProperties(chargeInfo, chargeInfoVO);
                LinkedList<ChargeDetailDO> chargeDetailDOList = chargeDetailMapper.findByChargeInfoId(chargeInfo.getId());
                if (Objects.nonNull(chargeDetailDOList) && !chargeDetailDOList.isEmpty()) {
                    Collections.sort(chargeDetailDOList);
                    chargeInfoVO.setChargeDetailDOList(chargeDetailDOList);
                }
                chargeInfoVOList.add(chargeInfoVO);
            });
        }
        ParkingOrderVO parkingOrderVO = new ParkingOrderVO();
        BeanUtils.copyProperties(parkingOrderDO, parkingOrderVO);
        DiscountInfoDO discountInfoDO = discountInfoMapper.findByParkingOrderId(parkingOrderDO.getId());
        parkingOrderVO.setDiscountInfoDO(discountInfoDO);
        parkingOrderVO.setChargeInfos(chargeInfoVOList);
        return SpkCommonResult.success(parkingOrderVO);
    }

    /**
     * 根据条件查询订单.
     *
     * @param parkingOrderQueryDTO 订单详情信息
     * @return Integer
     */
    @Override
    public SpkCommonResult list(final ParkingOrderQueryDTO parkingOrderQueryDTO) {
        //如果手机号不为空，先根据手机号查询user表，获取userId
        String iphone = parkingOrderQueryDTO.getKeyword();
        if (!StringUtils.isBlank(iphone)) {
            JSONObject userByIphone = userTemplateService.getUserByIphone(iphone);
            if (userByIphone == null || userByIphone.getInteger("code") != 200 || ObjectUtils.isEmpty(userByIphone.getJSONObject("data"))) {
                return SpkCommonResult.success();
            }
            JSONObject data = userByIphone.getJSONObject("data");
            parkingOrderQueryDTO.setUserId(String.valueOf(data.getLong("id")));
        }

        PageHelper.startPage(parkingOrderQueryDTO.getPage(), parkingOrderQueryDTO.getSize());
        List<ParkingOrderVO> parkingDOList = parkingOrderMapper.list(parkingOrderQueryDTO);
        parkingDOList.stream().forEach(item -> {
            //查询用户信息
            item.setPhone(getUserPhone(item.getUserId()));
            //查询优惠券信息
            item.setDiscountInfoDO(getDiscountInfo(item.getId()));
            //查询计费详情
            item.setChargeInfos(getChargeInfoVOList(item.getId()));
        });
        PageInfo<ParkingOrderVO> parkingOrderVOPageInfo = new PageInfo<>(parkingDOList);
        return SpkCommonResult.success(parkingOrderVOPageInfo);
    }

    /**
     * 根据订单id获取优惠券信息.
     *
     * @param userId 用户id
     * @return {@linkplain DiscountInfoDO}
     * @author ZDD
     * @date 2022/7/18 12:18:25
     */
    private String getUserPhone(final Long userId) {
        if (userId == null) {
            return "";
        }
        JSONObject userByIphone = userTemplateService.detailUser(userId);
        if (userByIphone == null || userByIphone.getInteger("code") != 200 || ObjectUtils.isEmpty(userByIphone.getJSONObject("data"))) {
            return "";
        }
        return userByIphone.getJSONObject("data").getString("iphone");
    }

    /**
     * 根据订单id获取优惠券信息.
     *
     * @param parkingOrderId 订单id
     * @return {@linkplain DiscountInfoDO}
     * @author ZDD
     * @date 2022/7/18 12:18:25
     */
    private DiscountInfoDO getDiscountInfo(final Long parkingOrderId) {
        return discountInfoMapper.findByParkingOrderId(parkingOrderId);
    }

    /**
     * 根据订单id获取优惠券信息.
     *
     * @param parkingOrderId 订单id
     * @return {@linkplain DiscountInfoDO}
     * @author ZDD
     * @date 2022/7/18 12:18:25
     */
    private LinkedList<ChargeInfoVO> getChargeInfoVOList(final Long parkingOrderId) {
        List<ChargeInfoDO> chargeInfoDOList = chargeInfoMapper.findByParkingOrderId(parkingOrderId);
        LinkedList<ChargeInfoVO> chargeInfoVOList = new LinkedList<>();
        if (Objects.nonNull(chargeInfoDOList) && !chargeInfoDOList.isEmpty()) {
            chargeInfoDOList.forEach(chargeInfo -> {
                ChargeInfoVO chargeInfoVO = new ChargeInfoVO();
                BeanUtils.copyProperties(chargeInfo, chargeInfoVO);
                LinkedList<ChargeDetailDO> chargeDetailDOList = chargeDetailMapper.findByChargeInfoId(chargeInfo.getId());
                if (Objects.nonNull(chargeDetailDOList) && !chargeDetailDOList.isEmpty()) {
                    Collections.sort(chargeDetailDOList);
                    chargeInfoVO.setChargeDetailDOList(chargeDetailDOList);
                }
                chargeInfoVOList.add(chargeInfoVO);
            });
        }
        return chargeInfoVOList;
    }

    @Override
    public Boolean createAndUpdateParkingOrder(final OrderDTO orderDTO) {
        ParkingOrder parkingOrder = orderDTO.getParkingOrder();
        ParkingOrderDTO parkingOrderDTO = ParkingOrderDTO.builder()
                .userId(parkingOrder.getUserId().toString())
                .orderNo(orderDTO.getOrderNo())
                .payParkingId(parkingOrder.getPayParkingId())
                .tempType(Objects.nonNull(parkingOrder.getTempType()) ? parkingOrder.getTempType() ? 0 : 1 : 1)
                .carTypeClass(parkingOrder.getCarTypeClass())
                .carTypeName(parkingOrder.getCarTypeName())
                .carTypeId(parkingOrder.getCarTypeId())
                .beginTime(parkingOrder.getBeginTime())
                .endTime(parkingOrder.getEndTime())
                .nextAggregateBeginTime(parkingOrder.getNextAggregateBeginTime())
                .aggregatedMaxAmount(parkingOrder.getAggregatedMaxAmount())
                .parkingMinutes(parkingOrder.getParkingMinutes())
                .totalAmount(parkingOrder.getTotalAmount())
                .discountedMinutes(parkingOrder.getDiscountedMinutes())
                .discountedAmount(parkingOrder.getDiscountedAmount())
                .chargeAmount(parkingOrder.getChargeAmount())
                .extraAmount(parkingOrder.getExtraAmount())
                .dueAmount(parkingOrder.getDueAmount())
                .chargeDueAmount(parkingOrder.getChargeDueAmount())
                .paidAmount(parkingOrder.getPaidAmount())
                .payChannel(orderDTO.getPlateForm())
                .payType(orderDTO.getPayType())
                .payTime(orderDTO.getPayTime())
                .receivedAmount(orderDTO.getAmount())
                .termNo(orderDTO.getTermNo())
                .operator("mini-user")
                .expireTime(parkingOrder.getExpireTime())
                .invoiceState(parkingOrder.getInvoiceState())
                .refundState(parkingOrder.getRefundState())
                .status(parkingOrder.getStatus())
                .projectNo(parkingOrder.getProjectNo())
                .creator("system")
                .build();
        Long parkingOrderId = createOrUpdate(parkingOrderDTO);
        if (parkingOrderId == -1L) {
            log.error("操作停车订单数据库 ParkingOrder 失败 " + JSON.toJSONString(parkingOrderDTO));
            return false;
        }
        List<ChargeInfo> chargeInfos = parkingOrder.getChargeInfos();
        if (Objects.nonNull(chargeInfos) && !chargeInfos.isEmpty()) {
            chargeInfos.forEach(chargeInfo -> {
                ChargeInfoDTO chargeInfoDTO = ChargeInfoDTO.builder()
                        .parkingOrderId(parkingOrderId.toString())
                        .beginCycleSeq(chargeInfo.getBeginCycleSeq())
                        .cycleNumber(chargeInfo.getCycleNumber())
                        .parkingMinutes(chargeInfo.getParkingMinutes())
                        .balancedMinutes(chargeInfo.getBalancedMinutes())
                        .discountedMinutes(chargeInfo.getDiscountedMinutes())
                        .totalAmount(chargeInfo.getTotalAmount())
                        .extraAmount(chargeInfo.getExtraAmount())
                        .build();
                Long chargeInfoId = chargeInfoService.createOrUpdate(chargeInfoDTO);
                if (chargeInfoId == -1L) {
                    log.error("操作停车计费信息数据库 ChargeInfo 失败 " + JSON.toJSONString(chargeInfoDTO));
                }
                List<ChargeDetail> chargeDetails = chargeInfo.getChargeDetails();
                if (Objects.nonNull(chargeDetails) && !chargeDetails.isEmpty()) {
                    chargeDetails.forEach(chargeDetail -> {
                        ChargeDetailDTO chargeDetailDTO = ChargeDetailDTO.builder()
                                .chargeInfoId(chargeInfoId.toString())
                                .chargeTypeName(chargeDetail.getChargeTypeName())
                                .beginTime(chargeDetail.getBeginTime())
                                .endTime(chargeDetail.getEndTime())
                                .parkingMinutes(chargeDetail.getParkingMinutes())
                                .balancedMinutes(chargeDetail.getBalancedMinutes())
                                .freeMinutes(chargeDetail.getFreedMinutes())
                                .discountedMinutes(chargeDetail.getDiscountedMinutes())
                                .chargingMinutes(chargeDetail.getChargingMinutes())
                                .chargeAmount(chargeDetail.getChargeAmount())
                                .remark(chargeDetail.getRemark())
                                .build();
                        if (chargeDetailService.createOrUpdate(chargeDetailDTO) <= 0) {
                            log.error("操作停车订单计费详情数据库 ChargeDetail 失败 " + JSON.toJSONString(chargeDetailDTO));
                        }
                    });
                }
            });
        }
        // 检查是否存在优惠券,如果存在则创建优惠券数据
        DiscountInfo discountInfo = parkingOrder.getDiscountInfo();
        if (Objects.nonNull(discountInfo)) {
            DiscountInfoDTO discountInfoDTO = DiscountInfoDTO.builder()
                    .parkingOrderId(parkingOrderId.toString())
                    .discountNo(discountInfo.getDiscountNo())
                    .valueType(discountInfo.getValueType())
                    .value(discountInfo.getValue())
                    .quantity(discountInfo.getQuantity())
                    .usedStartTime(discountInfo.getUsedStartTime())
                    .usedEndTime(discountInfo.getUsedEndTime())
                    .build();
            if (discountInfoService.createOrUpdate(discountInfoDTO) <= 0) {
                log.error("操作停车订单优惠券数据库 DiscountInfo 失败 " + JSON.toJSONString(discountInfoDTO));
            }
        }
        return true;
    }

    @Override
    public LinkedList<LockOrderVO> findLockOrderByUserId(final ParkingOrderQueryDTO parkingOrderQueryDTO) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", Long.valueOf(parkingOrderQueryDTO.getUserId()));
        params.put("startDate", parkingOrderQueryDTO.getStartDate());
        params.put("endDate", parkingOrderQueryDTO.getEndDate());
        List<ParkingOrderDO> parkingOrderDOList = parkingOrderMapper.findOrderByUserId(params);
        if (Objects.nonNull(parkingOrderDOList) && !parkingOrderDOList.isEmpty()) {
            return ParkOrderToLockOrderVO.convertToLockOrderVO(parkingOrderDOList);
        }
        return null;
    }
}
