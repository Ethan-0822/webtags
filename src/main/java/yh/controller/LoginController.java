package yh.controller;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import yh.base.dto.UserDto;
import yh.base.lang.Consts;
import yh.base.lang.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @projectName: webtags
 * @package: yh.controller
 * @className: LoginController
 * @author: Ethan
 * @description: TODO
 * @version: 1.0
 */

@Slf4j
@Controller
public class LoginController extends BaseController {
    @Autowired
    WxMpService wxMpService;

    @Autowired
    WxMpMessageRouter wxMpMessageRouter;

    @Autowired
    WxMpConfigStorage wxMpConfigStorage;


    /**
     * 登录
     * 有效期5分钟
     */
    @GetMapping("/login")
    public String login(){
        String code = "YH" + RandomUtil.randomNumbers(4);
        while (redisUtil.hasKey(code)){
            code = "YH" + RandomUtil.randomString(4);
        }

        String ticket = RandomUtil.randomString(32);

        redisUtil.set("ticket-" + code, ticket, 5*60);

        req.setAttribute("code", code);
        req.setAttribute("ticket", ticket);

        return "login";
    }

    /**
     * 验证是否登录
     */
    @ResponseBody
    @GetMapping("/login-check")
    public Result loginCheck(String code, String ticket){
        if(!redisUtil.hasKey("Info-" + code)){
            return Result.failure("用户未登录！");
        }

        String ticketBak = redisUtil.get("ticket-" + code).toString();
        if(!ticket.equals(ticketBak)){
            return Result.failure("登录失败！");
        }

        String userJson = redisUtil.get("Info-" + code).toString();
        UserDto userDto = JSONUtil.toBean(userJson, UserDto.class);
        req.getSession().setAttribute(Consts.CURRENT_USER, userDto);

        return Result.success();
    }


    /**
     * 公众号回调接口
     */
    @RequestMapping("/wx/back")
    @ResponseBody
    public String wxCallBack(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String signature = req.getParameter("signature");
        String timestamp = req.getParameter("timestamp");
        String nonce = req.getParameter("nonce");
        String echostr = req.getParameter("echostr");


        if(StrUtil.isNotBlank(echostr)){
            log.info("==========>正在配置回调接口，随机字符串为：{}", echostr);
            return echostr;
        }

        boolean checkSignature = wxMpService.checkSignature(timestamp, nonce, signature);
        if(!checkSignature){
            log.error("------------------> 签名不合法");
            return null;
        }

        // 判断是否为加密消息
        String encryptType = StringUtils.isBlank(req.getParameter("encrypt_type")) ?
                "raw" : req.getParameter("encrypt_type");
        WxMpXmlMessage wxMpXmlMessage = null;
        if ("raw".equals(encryptType)) {
            // 明文传输的消息
            wxMpXmlMessage = WxMpXmlMessage.fromXml(req.getInputStream());
        } else if ("aes".equals(encryptType)) {
            // aes加密的消息
            String msgSignature = req.getParameter("msg_signature");
            wxMpXmlMessage = WxMpXmlMessage.fromEncryptedXml(req.getInputStream(), wxMpConfigStorage, timestamp, nonce, msgSignature);
        } else {
            log.error("-------------> 不可识别的加密类型 {}" + encryptType);
            return "不可识别的加密类型";
        }

        // 根据设置路由规则发送到对应的处理器
        WxMpXmlOutMessage outMessage = wxMpMessageRouter.route(wxMpXmlMessage);

        return outMessage == null? "" : outMessage.toXml();
    }

    /**
     * 微信公众号登录
     */
    @RequestMapping("/autologin")
    public String autologin(String token){
        Object userObj = redisUtil.get("autologin-" + token);

        if(userObj != null){
            UserDto userDto = JSONUtil.toBean(userObj.toString(), UserDto.class);
            req.getSession().setAttribute(Consts.CURRENT_USER, userDto);
            return "redirect:/";
        }

        return "redirect:/login";
    }


    /**
     * 注销
     */
    @GetMapping("/logout")
    public String logout() {
        req.getSession().removeAttribute(Consts.CURRENT_USER);
        return "redirect:/";
    }
}