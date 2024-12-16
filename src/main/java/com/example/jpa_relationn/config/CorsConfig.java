package com.example.jpa_relationn.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        // Cho phép các domain được truy cập (Thay "*" bằng tên miền cụ thể nếu cần)
        corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));

        // Cho phép các phương thức HTTP
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Cho phép các header
        corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));

        // Cho phép gửi thông tin xác thực (nếu cần, ví dụ cookie)
        corsConfiguration.setAllowCredentials(true);

        // Áp dụng cấu hình CORS
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsFilter(source);
    }
}
