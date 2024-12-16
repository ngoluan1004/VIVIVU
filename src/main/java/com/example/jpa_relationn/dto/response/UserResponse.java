package com.example.jpa_relationn.dto.response;

import java.time.LocalDateTime;

import com.example.jpa_relationn.enums.Role;
import com.example.jpa_relationn.enums.Status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Integer userId;
    private String username;
    private String email;
    private String password;
    private String fullName;
    private String imgUrl;
    private String backgroundImage;
    private String bio;
    private String birthDate;
    private String location;
    private Role role;
    private Status status;
    private LocalDateTime createdAt;
}
