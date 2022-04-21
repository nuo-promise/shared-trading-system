package cn.suparking.user.service.intf;

import cn.suparking.user.api.beans.UserWalletDTO;
import cn.suparking.user.vo.UserWalletVO;

public interface UserWalletService {

    /**
     * create or update userWallet.
     * @param userWalletDTO {@linkplain UserWalletDTO}
     * @return rows
     */
    int createOrUpdate(UserWalletDTO userWalletDTO);


    /**
     * find user wallet by id.
     *
     * @param id primary key.
     * @return {@linkplain UserWalletVO}
     */
    UserWalletVO findById(Long id);
}
