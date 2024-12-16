package com.example.jpa_relationn.config;

import java.util.Arrays;
import java.util.Collections;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.example.jpa_relationn.enums.Role;

import io.micrometer.common.lang.Nullable;
import jakarta.servlet.http.HttpServletRequest;

@Configuration // khi có cái này thì class này sẽ được init lên va run các public method có
               // chứa annotation bean
// sau đó nó sẽ inject hoặc register vào cái application context
@EnableWebSecurity // cái này đã tự động enable khi có security nên là optional
@EnableMethodSecurity // cái này dùng để phân quyền dựa trên method
public class SecurityConfig {

        private final String[] PUBLIC_ENDPOINT = { "/users/create-user",
                        "/api/**",
                        "/posts/**",
                        "/auth/**"
        };

        @Value("${jwt.signerKey}")
        private String signerKey;

        // cấu hình sau đây là sẽ bảo vệ 1 số endpoint (cần token mới truy cập được) ,
        // và cái nào sẽ public để cho bất kì user truy cập mà ko cần cơ chế bảo vệ

        // CÁCH 1
        // @Bean
        // public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws
        // Exception {

        // // HttpSecurity cung cấp 1 số method để làm các việc trên | authorizeRequests
        // // cung cấp 1 lamda funtion dùng để config cái requestMatcher để xác định
        // // endpoint nào và cấu hình ra sao
        // httpSecurity.authorizeRequests(request ->
        // request.requestMatchers(HttpMethod.POST, "/users")
        // // requestMatchers nhận 2 param(methodPost)
        // .permitAll() // permitAll() là cho phép truy cập mà ko cần security
        // .requestMatchers(HttpMethod.POST, "/auth/token",
        // "/auth/introspect").permitAll()
        // .anyRequest().authenticated()); // còn tất cả các request khác phải
        // authentication thì mới được access
        // // vào hệ thống

        // // spring security sẽ mặc định config nó sẽ bật cấu hình của csrf lên
        // // csrf(): là nó sẽ bảo vệ endpoint khỏi những cái attack crossside
        // // httpSecurity.csrf(httpSecurityCsrfConfigurer ->
        // httpSecurityCsrfConfigurer.disable());
        // httpSecurity.csrf(AbstractHttpConfigurer::disable);

        // return httpSecurity.build();
        // }

        // CÁCH 2
        @Bean
        public SecurityFilterChain filterChain2(HttpSecurity httpSecurity) throws Exception {
                httpSecurity.authorizeRequests(request -> request
                                // .requestMatchers("/auth/token").authenticated()
                                // ngoài ra ta còn có thể dùng hasAuthority("ROLE_ADMIN")
                                .anyRequest().permitAll());
                httpSecurity.cors(cors -> cors.configurationSource(corsConfigurationSource())) // Thêm cấu hình CORS
                                .csrf(AbstractHttpConfigurer::disable);

                // vì chúng ta đang đóng vai trò là resource server nên ta sẽ config
                // oauth2ResourceServer // interface decoder decode cái token
                httpSecurity.oauth2ResourceServer(oauth2 -> oauth2
                                .jwt(jwtConfigurer -> jwtConfigurer
                                                .decoder(jwtDecoder())
                                                .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                                .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                // là cái mà khi authentication fail thì sẽ điều hướng user đi dâu or error
                // message
                );

                // httpSecurity.cors().and().csrf(AbstractHttpConfigurer::disable);

                return httpSecurity.build();
        }

        // convet "SCOPE_ADMIN" -> "ROLE_ADMIN"
        @Bean
        JwtAuthenticationConverter jwtAuthenticationConverter() {
                JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
                jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

                JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
                jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
                return jwtAuthenticationConverter;
        }

        @Bean
        JwtDecoder jwtDecoder() {
                SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
                return NimbusJwtDecoder
                                .withSecretKey(secretKeySpec)
                                .macAlgorithm(MacAlgorithm.HS512)
                                .build();
        }

        @Bean
        PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder(10);
        }

        private CorsConfigurationSource corsConfigurationSource() {

                return new CorsConfigurationSource() {

                        @Override
                        @Nullable
                        public CorsConfiguration getCorsConfiguration(HttpServletRequest arg0) {
                                CorsConfiguration cfg = new CorsConfiguration();
                                cfg.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
                                cfg.setAllowedMethods(Collections.singletonList("*"));
                                cfg.setAllowCredentials(true);
                                cfg.setAllowedHeaders(Collections.singletonList("*"));
                                cfg.setExposedHeaders(Arrays.asList("Authorization"));
                                cfg.setMaxAge(3600L);
                                return cfg;
                        }

                };
        }

}
/**
 * khi cấu hình là oauth2 resource server và muốn đăng kí 1 cái provider
 * manager(atuhentication provider) để support cho jwt token:
 * khi chúng ta thực hiện 1 cái request mà chúng ta cung cấp 1 cái token của mục
 * Auth -> thì cái jwtauthentication provider nó sẽ in changer và nó sẽ bắt đầu
 * thực hiện authentication
 * 
 * 
 * khi thực hiện validate thì ta cần 1 cái decoder()
 * thì cái authorization sử dụng cái decoder này để nó thực hiện decode cái
 * token để biết cái token đó hợp lệ hay không hợp lệ
 * 
 * để spring có thể authorization bằng jwt thì thông tin role phải có trong cái
 * token
 * 
 * thông thường thông tin về role của user sẽ được lưu trong claim gọi là
 * "scope" trong token
 * và khi decode cái token này thì user sẽ được map 1 cái role trong đó
 * 
 * vậy nên ta cần thêm claim(scope) vào trong token
 */