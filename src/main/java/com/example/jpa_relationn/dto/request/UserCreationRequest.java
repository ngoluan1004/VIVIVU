package com.example.jpa_relationn.dto.request;

import org.springframework.web.multipart.MultipartFile;

import com.example.jpa_relationn.enums.Role;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreationRequest {
    private String email;
    private String fullName;
    @Size(min = 3, message = "USERNAME_INVALID")
    private String username;
    @Size(min = 8, message = "INVALID_PASSWORD")
    private String password;
    private MultipartFile imgUrl;
    private MultipartFile backgroundImage;
    private String bio;
    private String birthDate;
    private String location;
    @Enumerated(EnumType.STRING)
    private Role role;
}
