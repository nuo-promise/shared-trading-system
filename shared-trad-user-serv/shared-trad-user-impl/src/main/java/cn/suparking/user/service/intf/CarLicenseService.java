package cn.suparking.user.service.intf;

import cn.suparking.user.api.beans.CarLicenseDTO;
import cn.suparking.user.vo.CarLicenseVO;

public interface CarLicenseService {

    /**
     * create or update car license .
     * @param carLicenseDTO {@linkplain CarLicenseDTO}
     * @return rows
     */
    int createOrUpdate(CarLicenseDTO carLicenseDTO);


    /**
     * find Car License by id.
     *
     * @param id primary key.
     * @return {@linkplain CarLicenseVO}
     */
    CarLicenseVO findById(Long id);
}
