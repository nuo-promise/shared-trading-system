package cn.suparking.data.service.impl;

import cn.suparking.data.api.beans.ParkingTriggerDTO;
import cn.suparking.data.dao.entity.ParkingTriggerDO;
import cn.suparking.data.dao.mapper.ParkingTriggerMapper;
import cn.suparking.data.service.ParkingTriggerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class ParkingTriggerServiceImpl implements ParkingTriggerService {

    private final ParkingTriggerMapper parkingTriggerMapper;

    public ParkingTriggerServiceImpl(final ParkingTriggerMapper parkingTriggerMapper) {
        this.parkingTriggerMapper = parkingTriggerMapper;
    }

    @Override
    public ParkingTriggerDO findById(final String id) {
        return parkingTriggerMapper.selectById(id);
    }

    @Override
    public Long createOrUpdate(final ParkingTriggerDTO parkingTriggerDTO) {
        ParkingTriggerDO parkingTriggerDO = ParkingTriggerDO.buildParkingTriggerDO(parkingTriggerDTO);
        if (StringUtils.isEmpty(parkingTriggerDTO.getId())) {
            if (parkingTriggerMapper.insert(parkingTriggerDO) == 1) {
                return parkingTriggerDO.getId();
            } else {
                return -1L;
            }
        } else {
            parkingTriggerMapper.update(parkingTriggerDO);
        }
        return parkingTriggerDO.getId();
    }
}
