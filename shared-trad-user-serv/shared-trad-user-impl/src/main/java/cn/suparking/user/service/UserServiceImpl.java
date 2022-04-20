package cn.suparking.user.service;

import cn.suparking.user.api.beans.UserDTO;
import cn.suparking.user.dao.entity.UserDO;
import cn.suparking.user.dao.mapper.UserMapper;
import cn.suparking.user.dao.vo.UserVO;
import cn.suparking.user.service.intf.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    public UserServiceImpl(final UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public int createOrUpdate(final UserDTO userDTO) {
        UserDO userDO = UserDO.buildUserDO(userDTO);
        // create new user
        if (StringUtils.isEmpty(userDTO.getId())) {
            userMapper.insertSelective(userDO);
        } else {
            userMapper.updateSelective(userDO);
        }
        return 0;
    }

    @Override
    public UserVO findById(final String id) {
        return UserVO.buildUserVO(userMapper.selectById(id));
    }
}
