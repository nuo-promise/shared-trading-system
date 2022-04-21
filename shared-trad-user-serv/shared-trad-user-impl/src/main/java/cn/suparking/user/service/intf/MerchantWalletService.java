package cn.suparking.user.service.intf;

import cn.suparking.user.api.beans.CarParkDTO;
import cn.suparking.user.vo.CarParkVO;

public interface MerchantWallService {

    /**
     * create or update carPark .
     * @param carParkDTO {@linkplain CarParkDTO}
     * @return rows
     */
    int createOrUpdate(CarParkDTO carParkDTO);


    /**
     * find Car park by id.
     *
     * @param id primary key.
     * @return {@linkplain CarParkVO}
     */
    CarParkVO findById(Long id);
}
