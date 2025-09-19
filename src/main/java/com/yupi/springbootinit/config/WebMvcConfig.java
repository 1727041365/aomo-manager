package com.yupi.springbootinit.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Slf4j
//@Configuration
//public class WebMvcConfig implements WebMvcConfigurer {
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        log.info("映射图片文件.../news/img/");
//        // 将 URL 中的 /images/** 映射到本地目录 /usr/local/news/images/
//        registry.addResourceHandler("/images/**")
//                .addResourceLocations("file:C:/school/img/");
//        // 将 URL 中的 /video/** 映射到本地目录 C:/school/video/
//        registry.addResourceHandler("/video/**")
//                .addResourceLocations("file:C:/school/video/");
//    }
//}