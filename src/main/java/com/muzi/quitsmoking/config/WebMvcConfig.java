package com.muzi.quitsmoking.config;

import com.muzi.quitsmoking.interceptor.AuthenticationInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC配置类
 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthenticationInterceptor authenticationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册认证拦截器，添加不需要拦截的路径
//        registry.addInterceptor(authenticationInterceptor)
//                .addPathPatterns("/**") // 拦截所有请求
//                .excludePathPatterns(
//                        "/user/login",
//                        "/user/verify-code",
//                        "/swagger-ui/**",
//                        "/swagger-resources/**",
//                        "/v3/api-docs/**",
//                        "/doc.html",  // 添加 Knife4j 文档路径
//                        "/error"
//                );
    }
} 