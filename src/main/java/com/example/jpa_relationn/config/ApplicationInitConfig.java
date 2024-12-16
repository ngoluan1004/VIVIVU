package com.example.jpa_relationn.config;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.jpa_relationn.enums.Role;
import com.example.jpa_relationn.model.User;
import com.example.jpa_relationn.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

// cấu hình để tạo 1 cái user_admin mỗi khi start cái ứng dụng này lên
@Configuration
@Slf4j
public class ApplicationInitConfig {

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ApplicationRunner sẽ chạy mỗi khi ứng dụng start lên
    // @Bean
    // ApplicationRunner applicationRunner(UserRepository userRepository) {
    // return args -> {
    // // add 1 user_admin vào user của mình
    // if (userRepository.findByUsername("admin").isEmpty()) {
    // var role = Role.ADMIN;

    // User user = User.builder()
    // .username("admin")
    // .password(passwordEncoder.encode("admin"))
    // .fullName("Ngo Luan")
    // .role(role)
    // .build();

    // userRepository.save(user);
    // log.warn("admin user has beean created with default password: admin, please
    // change it");
    // }
    // };
    // }
}
