package cn.suparking.order.service.impl;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.order.api.beans.ParkingOrderDTO;
import cn.suparking.order.api.beans.ParkingQuery;
import cn.suparking.order.dao.entity.ChargeDetailDO;
import cn.suparking.order.dao.entity.ChargeInfoDO;
import cn.suparking.order.dao.entity.DiscountInfoDO;
import cn.suparking.order.dao.mapper.ChargeDetailMapper;
import cn.suparking.order.dao.mapper.ChargeInfoMapper;
import cn.suparking.order.dao.mapper.DiscountInfoMapper;
import cn.suparking.order.dao.vo.ChargeInfoVO;
import cn.suparking.order.dao.vo.ParkingOrderVO;
import cn.suparking.order.service.ParkingOrderService;
import cn.suparking.order.dao.entity.ParkingOrderDO;
import cn.suparking.order.dao.mapper.ParkingOrderMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class ParkingOrderServiceImpl implements ParkingOrderService {

    private final ChargeDetailMapper chargeDetailMapper;

    private final DiscountInfoMapper discountInfoMapper;
    private final ChargeInfoMapper chargeInfoMapper;

    private final ParkingOrderMapper parkingOrderMapper;

    public ParkingOrderServiceImpl(final ChargeDetailMapper chargeDetailMapper, final DiscountInfoMapper discountInfoMapper,
                                   final ChargeInfoMapper chargeInfoMapper,
                                   final ParkingOrderMapper parkingOrderMapper) {
        this.chargeDetailMapper = chargeDetailMapper;
        this.discountInfoMapper = discountInfoMapper;
        this.chargeInfoMapper = chargeInfoMapper;
        this.parkingOrderMapper = parkingOrderMapper;
    }

    @Override
    public ParkingOrderDO findById(final String id) {
        return parkingOrderMapper.selectById(id);
    }

    @Override
    public Integer createOrUpdate(final ParkingOrderDTO parkingOrderDTO) {
        ParkingOrderDO parkingOrderDO = ParkingOrderDO.buildParkingOrderDO(parkingOrderDTO);
        if (StringUtils.isEmpty(parkingOrderDTO.getId())) {
            return parkingOrderMapper.insert(parkingOrderDO);
        }
        return parkingOrderMapper.update(parkingOrderDO);
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
}
