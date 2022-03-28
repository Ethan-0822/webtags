package yh.controller;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import yh.base.dto.CollectDto;
import yh.base.dto.SidebarDateDto;
import yh.base.annotation.Login;
import yh.base.lang.Result;
import yh.domain.Collect;
import yh.domain.User;
import yh.service.SearchService;

import java.util.List;

/**
 * @projectName: webtags
 * @package: yh.controller
 * @className: CollectController
 * @author: Ethan
 * @description: TODO
 * @version: 1.0
 */
@Slf4j
@RestController
public class CollectController extends BaseController{
    @Autowired
    SearchService searchService;

    @Value("${yh.domain}")
    String domain;

    /**
     * 查询某用户的收藏
     */
    @Login
    @GetMapping("/api/collects/{userId}/{dateline}")
    public Result userCollects(@PathVariable(name="userId") long userId,
                               @PathVariable(name="dateline")String dateline){

        Page<CollectDto> page = collectService.findUserCollects(userId, dateline, getPage());
        return Result.success(page);
    }

    /**
     * 根据id删除收藏
     */
    @Login
    @PostMapping("/api/collect/delete")
    public Result delCollect(Long id){
        Collect collectById = collectService.findById(id);

        Assert.notNull(collectById, "收藏不存在！");
        Assert.isTrue(getCurrentUserId() == collectById.getUser().getId(), "无权限删除");

        collectService.deleteById(id);

        return Result.success();
    }

    /**
     * 创建or修改收藏
     */
    @Login
    @PostMapping("/api/collect/save")
    public Result saveCollect(Collect collect){

        Assert.hasLength(collect.getTitle(), "标题不能为空");
        Assert.hasLength(collect.getUrl(), "链接不能为空");

        if(collect.getId() != null){
            Collect collectById = collectService.findById(collect.getId());
            Assert.notNull(collectById, "未找到对应的收藏！");
            Assert.isTrue(getCurrentUserId() == collectById.getUser().getId(), "无权限操作！");
        }

        User user = new User();
        user.setId(getCurrentUserId());
        collect.setUser(user);

        collectService.save(collect);

        return Result.success();
    }

    /**
     * 通过判断关键字q来判断用户是搜索自己，还是点击了收藏广场
     * @param userId 用户id
     * @param q 关键词
     * @return 查询后的分页数据
     */
    @GetMapping("/api/collects/square")
    public Result allCollectSquare(Long userId, String q) {
        Page<CollectDto> page;
        if (StrUtil.isNotBlank(q)) {
            page = searchService.search(q, userId, getPage());
        } else {
            page = collectService.findSquareCollects(getPage());
        }

        return Result.success(page);
    }


    /**
     * 编辑收藏
     */
    @Login
    @GetMapping("/collect/edit")
    public ModelAndView editCollect(Collect collect){

        CollectDto collectDto = new CollectDto();

        String js = "javascript:(function(){" +
                "var site='" + domain +"/collect/edit?title='+encodeURIComponent(document.title)+'&url='+encodeURIComponent(document.URL);" +
                "var win=window.open(site,'_blank');" +
                "win.focus();" +
                "})()";

        if(collect.getId() != null){
            Collect collectById = collectService.findById(collect.getId());
            Assert.notNull(collectById, "未找到对应的收藏！");
            Assert.isTrue(getCurrentUserId() == collectById.getUser().getId(), "无权限编辑");

            BeanUtils.copyProperties(collectById, collect);
        }

        collectDto = collectMapper.toDto(collect);

        req.setAttribute("collect", collectDto);
        req.setAttribute("js", js);

        return new ModelAndView("collect-edit");
    }


    /**
     * 用户的收藏日期列表
     */
    @Login
    @GetMapping(value = "/collect-user/{userId}")
    public ModelAndView userCollect(@PathVariable(name = "userId")Long userId){
        List<SidebarDateDto> sidebarDateDtos = collectService.getSidebarDateDtoByUserId(userId);
        req.setAttribute("datelines",sidebarDateDtos);
        req.setAttribute("userId",userId);

        return new ModelAndView("index");
    }


    /**
     * 跳转收藏广场
     */
    @GetMapping("/collect-square")
    public ModelAndView collectSquare() {
        return new ModelAndView("collect-square");
    }
}