package cn.suparking.data.service.impl;

import cn.suparking.data.api.beans.ParkingEventDTO;
import cn.suparking.data.dao.entity.ParkingEventDO;
import cn.suparking.data.dao.mapper.ParkingEventMapper;
import cn.suparking.data.service.ParkingEventService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class ParkingEventServiceImpl implements ParkingEventService {

    private final ParkingEventMapper parkingEventMapper;

    public ParkingEventServiceImpl(final ParkingEventMapper parkingEventMapper) {
        this.parkingEventMapper = parkingEventMapper;
    }

    @Override
    public ParkingEventDO findById(final String id) {
        return parkingEventMapper.selectById(id);
    }

    @Override
    public Integer createOrUpdate(final ParkingEventDTO parkingEventDTO) {
        ParkingEventDO parkingEventDO = ParkingEventDO.buildParkingEventDO(parkingEventDTO);
        if (StringUtils.isEmpty(parkingEventDTO.getId())) {
            return parkingEventMapper.insert(parkingEventDO);
        }
        return parkingEventMapper.update(parkingEventDO);
    }
}
