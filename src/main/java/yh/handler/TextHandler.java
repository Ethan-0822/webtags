package yh.handler;

import cn.hutool.core.util.StrUtil;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @projectName: webtags
 * @package: yh.handler
 * @className: TextHandler
 * @author: Ethan
 * @description: 字符串消息处理器
 * @version: 1.0
 */

@Component
public class TextHandler implements WxMpMessageHandler {

    private final String UNKOKEN = "不能识别您输入的信息，请确认后输入！";

    @Autowired
    LoginHandler loginHandler;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {

        // 用户id
        String openid = wxMessage.getFromUser();
        // 用户输入的内容
        String content = wxMessage.getContent();

        String result = UNKOKEN;

        if(StrUtil.isNotBlank(content)){
            content = content.toUpperCase().trim();

            // 消息分发到其他处理器
            if(content.indexOf("YH") == 0){
                result = loginHandler.handle(openid, content, wxMpService);
            }
        }

        return WxMpXmlOutMessage
                .TEXT()
                .content(result)
                .fromUser(wxMessage.getToUser())
                .toUser(wxMessage.getFromUser())
                .build();
    }
}