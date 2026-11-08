package com.bond.yunjian.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * MVC 配置：注册登录态拦截器并声明免认证白名单。
 * 注意：context-path（/api/v1）不参与拦截器路径匹配，白名单中不要带该前缀。
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    /** 免认证路径：认证入口、健康检查、测试端点与错误转发 */
    private static final String[] AUTH_WHITE_LIST = {
            "/auth/login",
            "/auth/register",
            "/auth/logout",
            "/health",
            "/test",
            "/error"
    };

    private final LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(AUTH_WHITE_LIST);
    }
}
