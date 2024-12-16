package com.example.jpa_relationn.dto.request;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateRequest {
    private String email;
    private String fullName;
    private MultipartFile imgUrl;
    private MultipartFile backgroundImage;
    private String bio;
    private String birthDate;
    private String location;
    private String role;
    private String status;
}
