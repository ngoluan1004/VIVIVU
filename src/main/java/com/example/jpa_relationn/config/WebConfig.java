package com.example.jpa_relationn.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadPath = Paths.get(uploadDir).toUri().toString();
        registry.addResourceHandler("/images/**")
                .addResourceLocations(uploadPath);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Cho phép tất cả các endpoint
                .allowedOrigins("http://localhost:3000") // Nguồn được phép truy cập
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Phương thức HTTP được phép
                .allowedHeaders("*") // Header được phép
                .allowCredentials(true); // Cho phép cookie nếu cần
    }
}
