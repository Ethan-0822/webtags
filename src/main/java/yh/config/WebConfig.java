package yh.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import yh.base.interceptor.AuthInterceptor;

/**
 * @projectName: webtags
 * @package: yh.config
 * @className: WebConfig
 * @author: Ethan
 * @description: TODO
 * @version: 1.0
 */

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/js/**","/css/**","/images/**","/layui/**");
    }
}