package com.example.filemanager.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyConfig implements WebMvcConfigurer {

    /**
     * 动态页面跳转路径
     * @param registry

     @Override public void addViewControllers(ViewControllerRegistry registry){
     }
     */

    /**
     * 哪些路径，不受拦截器的约束
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginHandlerInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/login", "/userList","/userToLogin", "/userRegister", "/login/**",
                         "/res/**", "/utils/**","/favicon.ico");
    }
}
