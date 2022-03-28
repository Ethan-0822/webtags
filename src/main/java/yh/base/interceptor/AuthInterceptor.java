package yh.base.interceptor;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import yh.base.dto.UserDto;
import yh.base.annotation.Login;
import yh.base.lang.Consts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @projectName: webtags
 * @package: yh.base.interceptor
 * @className: AuthInterceptor
 * @author: Ethan
 * @description: 登录拦截器
 * @version: 1.0
 */

public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 保持前后端状态一致
        UserDto userDto = (UserDto) request.getSession().getAttribute(Consts.CURRENT_USER);
        if(userDto == null){
            userDto = new UserDto();
            userDto.setId(-1L);
        }
        request.setAttribute("current",userDto);

        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        // 反射获取当前方法中是否标注有@Login注解
        Login annotation = ((HandlerMethod)handler).getMethodAnnotation(Login.class);
        if(annotation != null){

            if(userDto.getId() == -1 || userDto.getId() == null){
                response.sendRedirect(request.getContextPath() + "/login");
                return false;
            }
        }

        return true;
    }

}