//package com.scu.turing.web.config;
//
//import com.scu.turing.interceptor.AdminInterceptor;
//import com.scu.turing.interceptor.AuthInterceptor;
//import nz.net.ultraq.thymeleaf.LayoutDialect;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
//
//@Configuration
//public class WebMvcConfig extends WebMvcConfigurerAdapter {
//
//    private AuthInterceptor authInterceptor;
//    private AdminInterceptor adminInterceptor;
//
//    public WebMvcConfig(@Autowired AuthInterceptor authInterceptor,
//                        @Autowired AdminInterceptor adminInterceptor) {
//        this.authInterceptor = authInterceptor;
//        this.adminInterceptor = adminInterceptor;
//    }
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(authInterceptor);
//        registry.addInterceptor(adminInterceptor);
//    }
//}