package cn.suparking.user.service.intf;

import cn.suparking.user.api.beans.MerchantDTO;
import cn.suparking.user.vo.MerchantVO;

public interface MerchantService {

    /**
     * create or update merchant .
     * @param merchantDTO {@linkplain MerchantDTO}
     * @return rows
     */
    int createOrUpdate(MerchantDTO merchantDTO);


    /**
     * find Merchant by id.
     *
     * @param id primary key.
     * @return {@linkplain MerchantVO}
     */
    MerchantVO findById(Long id);
}
