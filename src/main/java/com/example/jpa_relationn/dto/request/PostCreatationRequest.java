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
public class PostCreatationRequest {
    private String title;
    private String content;
    private String address;
    private String category;
    private MultipartFile image;
}
