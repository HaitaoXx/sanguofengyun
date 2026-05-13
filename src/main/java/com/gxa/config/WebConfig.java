package com.gxa.config;

import com.gxa.interceptor.AdminInterceptor;
import com.gxa.interceptor.JwtInterceptor;
import com.gxa.interceptor.PermissionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Autowired
    private JwtInterceptor jwtInterceptor;
    
    @Autowired
    private AdminInterceptor adminInterceptor;
    
    @Autowired
    private PermissionInterceptor permissionInterceptor;
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOriginPatterns("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true)
            .maxAge(3600);
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/user/login", "/api/user/register", "/share/**", "/api/ai/generate/**", "/api/ai/clear/**");
        
        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/api/audit/**", "/api/admin/**");
        
        registry.addInterceptor(permissionInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/user/login", "/api/user/register", "/share/**", "/api/ai/generate/**", "/api/ai/clear/**");
    }
}
