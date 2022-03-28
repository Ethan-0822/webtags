package yh.service;

import yh.base.dto.UserDto;

/**
 *
 */
public interface UserService {
    UserDto register(String openid);

    UserDto getDtoById(Long userId);
}
