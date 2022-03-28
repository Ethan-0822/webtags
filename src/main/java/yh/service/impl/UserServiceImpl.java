package yh.service.impl;

import cn.hutool.core.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import yh.base.dto.UserDto;
import yh.domain.User;
import yh.mapstruct.UserMapper;
import yh.repository.UserRepository;
import yh.service.UserService;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @projectName: webtags
 * @package: yh.service.impl
 * @className: UserServiceImpl
 * @author: Ethan
 * @description: TODO
 * @version: 1.0
 */

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

    @Value("${yh.domain}" + "/images/avatar.jpg" )
    String avatarurl;

    @Override
    @Transactional
    public UserDto register(String openid) {

        Assert.isTrue(openid != null, "不合法注册条件");

        User user = userRepository.findByOpenId(openid);
        if(user == null){
            user = new User();
            user.setUsername("YH-" + RandomUtil.randomString(5));
            user.setOpenId(openid);
            user.setAvatar(avatarurl);
            user.setCreated(LocalDateTime.now());

        }else{
            user.setLasted(LocalDateTime.now());
        }

        userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    public UserDto getDtoById(Long userId) {
        Optional<User> optional = userRepository.findById(userId);
        if(optional.isPresent()){
            return userMapper.toDto(optional.get());
        }
        return null;
    }
}