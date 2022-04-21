package cn.suparking.user.service;

import cn.suparking.user.api.beans.UserWalletDTO;
import cn.suparking.user.dao.entity.UserWalletDO;
import cn.suparking.user.dao.mapper.UserWalletMapper;
import cn.suparking.user.service.intf.UserWalletService;
import cn.suparking.user.vo.UserWalletVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserWalletServiceImpl implements UserWalletService {

    private final UserWalletMapper userWalletMapper;

    public UserWalletServiceImpl(final UserWalletMapper userWalletMapper) {
        this.userWalletMapper = userWalletMapper;
    }

    @Override
    public int createOrUpdate(final UserWalletDTO userWalletDTO) {
        UserWalletDO userWalletDO = UserWalletDO.buildUserWalletDO(userWalletDTO);
        // create new user
        if (StringUtils.isEmpty(userWalletDTO.getId())) {
            return userWalletMapper.insertSelective(userWalletDO);
        }
        return userWalletMapper.updateSelective(userWalletDO);
    }

    @Override
    public UserWalletVO findById(final Long id) {
        return UserWalletVO.buildUserWalletVO(userWalletMapper.selectById(id));
    }
}
