package cn.suparking.user.service;

import cn.suparking.user.api.beans.UserDTO;
import cn.suparking.user.dao.entity.UserDO;
import cn.suparking.user.dao.mapper.UserMapper;
import cn.suparking.user.service.intf.UserService;
import cn.suparking.user.tools.ReactiveRedisUtils;
import cn.suparking.user.vo.UserVO;
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

    /**
     * seata 与ss 整合事务,除了全局事务注解,然后再特定的Service impl 函数头也要增加 @Transactional 注解.
     * @param userDTO {@linkplain UserDTO}
     * @return update or create int
     */
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

    @Override
    public UserVO findUserByIphone(final String iphone) {
        return UserVO.buildUserVO(userMapper.selectUserByIphone(iphone));
    }
}
