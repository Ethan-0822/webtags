package yh.config;

import lombok.Data;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import yh.handler.TextHandler;

/**
 * @projectName: webtags
 * @package: yh.config
 * @className: WeChatMpConfig
 * @author: Ethan
 * @description: 微信开发配置类
 * @version: 1.0
 */

@Data
@Configuration
@ConfigurationProperties("wechat")
public class WeChatMpConfig {
    private String mpAppId;     // 开发者ID
    private String mpAppSecret; // 开发者密码
    private String token;       // 令牌

    @Autowired
    TextHandler textHandler;

    /**
     * 微信客户端配置存储
     */
    @Bean
    public WxMpConfigStorage wxMpConfigStorage(){
        WxMpInMemoryConfigStorage wxConfigStorage = new WxMpInMemoryConfigStorage();
        wxConfigStorage.setAppId(mpAppId);
        wxConfigStorage.setSecret(mpAppSecret);
        wxConfigStorage.setToken(token);
        return wxConfigStorage;
    }

    /**
     * 微信API的Service
     */
    @Bean
    public WxMpService wxMpService(){
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxMpConfigStorage());
        return wxMpService;
    }

    /**
     * 微信消息路由器
     */
    @Bean
    public WxMpMessageRouter router(WxMpService wxMpService){
        WxMpMessageRouter router = new WxMpMessageRouter(wxMpService);

        // 配置字符串消息路由
        router
                .rule()
                .async(false)
                .msgType(WxConsts.XmlMsgType.TEXT)
                .handler(textHandler)
                .end();

        return router;
    }
}