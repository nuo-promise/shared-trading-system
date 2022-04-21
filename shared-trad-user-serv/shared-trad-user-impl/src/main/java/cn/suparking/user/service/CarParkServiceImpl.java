package cn.suparking.user.service;

import cn.suparking.user.api.beans.CarParkDTO;
import cn.suparking.user.dao.entity.CarParkDO;
import cn.suparking.user.dao.mapper.CarParkMapper;
import cn.suparking.user.service.intf.CarParkService;
import cn.suparking.user.vo.CarParkVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CarParkServiceImpl implements CarParkService {

    private final CarParkMapper carParkMapper;

    public CarParkServiceImpl(final CarParkMapper carParkMapper) {
        this.carParkMapper = carParkMapper;
    }

    @Override
    public int createOrUpdate(final CarParkDTO carParkDTO) {
        CarParkDO carParkDO = CarParkDO.buildCarParkDO(carParkDTO);
        // create new user
        if (StringUtils.isEmpty(carParkDTO.getId())) {
            return carParkMapper.insertSelective(carParkDO);
        }
        return carParkMapper.updateSelective(carParkDO);
    }

    @Override
    public CarParkVO findById(final Long id) {
        return CarParkVO.buildCarParkVO(carParkMapper.selectById(id));
    }
}
