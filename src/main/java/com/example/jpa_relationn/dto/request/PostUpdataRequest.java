package com.example.jpa_relationn.dto.request;

import com.example.jpa_relationn.enums.PostStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostUpdataRequest {
    private String title;
    private String content;
    private String address;
    private String category;
}
