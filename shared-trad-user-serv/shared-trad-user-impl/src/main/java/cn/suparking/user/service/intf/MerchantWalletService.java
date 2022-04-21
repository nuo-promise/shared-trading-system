package cn.suparking.user.service.intf;

import cn.suparking.user.api.beans.MerchantWalletDTO;
import cn.suparking.user.vo.MerchantWalletVO;

public interface MerchantWalletService {

    /**
     * create or update merchant wallet .
     * @param merchantWalletDTO {@linkplain MerchantWalletDTO}
     * @return rows
     */
    int createOrUpdate(MerchantWalletDTO merchantWalletDTO);


    /**
     * find Merchant wallet by id.
     *
     * @param id primary key.
     * @return {@linkplain MerchantWalletVO}
     */
    MerchantWalletVO findById(Long id);
}
