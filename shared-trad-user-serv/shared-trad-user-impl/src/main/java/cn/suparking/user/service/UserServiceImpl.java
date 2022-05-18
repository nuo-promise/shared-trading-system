package cn.suparking.user.service;

import cn.suparking.user.api.beans.UserDTO;
import cn.suparking.user.dao.entity.UserDO;
import cn.suparking.user.dao.mapper.UserMapper;
import cn.suparking.user.tools.ReactiveRedisUtils;
import cn.suparking.user.vo.UserVO;
import cn.suparking.user.service.intf.UserService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * User Service inf.
 */
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
            return userMapper.insertSelective(userDO);
        }
        return userMapper.updateSelective(userDO);
    }

    @Override
    public UserVO findById(final Long id) {
        return UserVO.buildUserVO(userMapper.selectById(id));
    }

    @Override
    public UserVO findByOpenId(final String miniOpenId) {
        String userInfo = (String) ReactiveRedisUtils.getData(miniOpenId).block(Duration.ofMillis(3000));
        if (StringUtils.isNotBlank(userInfo)) {
            return JSON.parseObject(userInfo, UserVO.class);
        } else {
            return UserVO.buildUserVO(userMapper.selectByMiniOpenId(miniOpenId));
        }
    }
}
