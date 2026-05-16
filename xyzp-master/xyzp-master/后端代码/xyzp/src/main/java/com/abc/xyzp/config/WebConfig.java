package com.abc.xyzp.config;

import com.abc.xyzp.common.interceptor.TokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import javax.annotation.Resource;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    TokenInterceptor tokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/api/authorization/login", "/api/email", "/api/authorization/register",
                        "/api/image/download/**", "/api/job", "/api/team/getNewTeamJob", "/api/team/getTeamJobInfo/**",
                        "/api/team/recommendTeamJob", "/api/team/searchIndexTeamJob");
    }



}