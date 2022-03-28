package yh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import yh.base.dto.UserDto;

/**
 * @projectName: webtags
 * @package: yh.controller
 * @className: SearchController
 * @author: Ethan
 * @description: TODO
 * @version: 1.0
 */

@Controller
public class SearchController extends BaseController {

    /**
     * 搜索收藏
     */
    @GetMapping("/search")
    public String search(String q, Long userId){

        // 回写参数给Jpa分页
        req.setAttribute("q", q);
        req.setAttribute("userId", userId);

        String message = "正在搜索公开【收藏广场】的收藏记录";

        if(userId != null){
            UserDto userDto = userService.getDtoById(userId);
            if (userDto != null) {
                message = "正在搜索用户【" + userDto.getUsername() +  "】的收藏记录";
            }
        }

        req.setAttribute("searchTip", message);
        return "collect-square";
    }
}