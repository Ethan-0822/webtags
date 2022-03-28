package yh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
;
import yh.base.dto.SidebarDateDto;
import yh.base.annotation.Login;

import java.util.List;

/**
 * @projectName: webtags
 * @package: yh.controller
 * @className: IndexController
 * @author: Ethan
 * @description: TODO
 * @version: 1.0
 */

@Controller
public class IndexController extends BaseController{

    /**
     * 首页
     */
    @Login
    @RequestMapping(value = {"","/"})
    public String index(){
        List<SidebarDateDto> sidebarDateDtos = collectService.getSidebarDateDtoByUserId(getCurrentUserId());
        req.setAttribute("datelines",sidebarDateDtos);
        req.setAttribute("userId",getCurrentUserId());

        return "index";
    }


}