package cn.suparking.user.service;

import cn.suparking.user.api.beans.CarLicenseDTO;
import cn.suparking.user.dao.entity.CarLicenseDO;
import cn.suparking.user.dao.mapper.CarLicenseMapper;
import cn.suparking.user.service.intf.CarLicenseService;
import cn.suparking.user.vo.CarLicenseVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CarLicenseServiceImpl implements CarLicenseService {

    private final CarLicenseMapper carLicenseMapper;

    public CarLicenseServiceImpl(final CarLicenseMapper carLicenseMapper) {
        this.carLicenseMapper = carLicenseMapper;
    }

    @Override
    public int createOrUpdate(final CarLicenseDTO carLicenseDTO) {
        CarLicenseDO carrLicenseDO = CarLicenseDO.buildCarLicenseDO(carLicenseDTO);
        // create new user
        if (StringUtils.isEmpty(carLicenseDTO.getId())) {
            return carLicenseMapper.insertSelective(carrLicenseDO);
        }
        return carLicenseMapper.updateSelective(carrLicenseDO);
    }

    @Override
    public CarLicenseVO findById(final Long id) {
        return CarLicenseVO.buildCarLicenseVO(carLicenseMapper.selectById(id));
    }
}
