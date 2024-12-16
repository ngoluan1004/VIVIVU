package com.example.jpa_relationn.dto.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostRequest {
    private String title;
    private String content;
    private String address;
    private String category;
    private List<String> images; // Danh sách URL ảnh\
}
