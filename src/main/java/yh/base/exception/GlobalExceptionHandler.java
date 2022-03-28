package yh.base.exception;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import yh.base.lang.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @projectName: webtags
 * @package: yh.base.exception
 * @className: GlobalExceptionHandler
 * @author: Ethan
 * @description: 全局异常处理
 * @version: 1.0
 */
@Slf4j
@Component
public class GlobalExceptionHandler implements HandlerExceptionResolver{
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        if(ex instanceof IllegalArgumentException || ex instanceof IllegalStateException ){
            log.error(ex.getMessage());
        }else {
            log.error(ex.getMessage(),ex);
        }

        // 通过请求头判断是否是ajax请求
        String header = request.getHeader("X-Requested-With");
        if("XMLHttpRequest".equals(header)){
            try {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(JSONUtil.toJsonStr(Result.failure(ex.getMessage())));
            } catch (IOException ignored) {
            }
            return new ModelAndView();
        }else{
            request.setAttribute("message","系统异常，请稍后重试！");
        }

        return new ModelAndView("error");
    }
}