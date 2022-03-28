package yh.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.ServletRequestUtils;
import yh.base.dto.UserDto;
import yh.base.lang.Consts;
import yh.mapstruct.CollectMapper;
import yh.service.CollectService;
import yh.service.UserService;
import yh.util.RedisUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @projectName: webtags
 * @package: yh.controller
 * @className: BaseController
 * @author: Ethan
 * @description: TODO
 * @version: 1.0
 */

public class BaseController {
    @Autowired
    RedisUtil redisUtil;

    @Resource
    HttpServletRequest req;

    @Autowired
    CollectService collectService;

    @Autowired
    CollectMapper collectMapper;

    @Autowired
    UserService userService;

    /**
     * 当前登录用户
     */
    public UserDto getCurrentUser(){
        UserDto userDto = (UserDto) req.getSession().getAttribute(Consts.CURRENT_USER);
        if(userDto == null){
            userDto = new UserDto();
            userDto.setId(-1L);
        }
        return userDto;
    }

    /**
     * 当前登录用户id
     */
    public Long getCurrentUserId(){
        return getCurrentUser().getId();
    }

    /**
     * jpa分页
     * 收藏日期优先，相同按创建时间
     */
    Pageable getPage() {
        int page = ServletRequestUtils.getIntParameter(req, "page", 1);
        int size = ServletRequestUtils.getIntParameter(req, "size", 10);

        return PageRequest.of(page - 1, size,
                Sort.by(
                        Sort.Order.desc("collected"),
                        Sort.Order.desc("created")
                )
        );
    }
}