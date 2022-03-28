package yh.handler;

import cn.hutool.core.lang.UUID;
import cn.hutool.json.JSONUtil;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import yh.base.dto.UserDto;
import yh.service.UserService;
import yh.util.RedisUtil;


/**
 * @projectName: webtags
 * @package: yh.handler
 * @className: LoginHandler
 * @author: Ethan
 * @description: 登录处理器
 * @version: 1.0
 */

@Component
public class LoginHandler {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    UserService userService;

    @Value("${yh.domain}")
    String domain;

    public String handle(String openid, String content, WxMpService wxMpService) {

        if(content.length() != 6 || !redisUtil.hasKey("ticket-" + content)){
            return "输入的验证码不正确或者已过期";
        }

        UserDto userDto = userService.register(openid);

        // 浏览器登录信息有效时间为5分钟
        redisUtil.set("Info-" + content, JSONUtil.toJsonStr(userDto), 5*60);

        // 微信公众号登录信息有效时间为48小时
        String token = UUID.randomUUID().toString(true);
        String result = domain + "/autologin?token=" + token;
        redisUtil.set("autologin-" + token, JSONUtil.toJsonStr(userDto), 48*60*60);

        return "欢迎您！\n\n" + "<a href  = '" + result + "'>已完成登录，点击跳转</a>";
    }
}