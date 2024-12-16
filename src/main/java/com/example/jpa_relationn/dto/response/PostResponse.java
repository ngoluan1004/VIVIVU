package com.example.jpa_relationn.dto.response;

import java.util.Date;

import com.example.jpa_relationn.enums.PostStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponse {
    private Integer postId;
    private String title;
    private String content;
    private PostStatus status;
    private String address;
    private String category;
    private Integer viewsCount;
    private Integer likesCount;
    private Integer repostCount;
    private Integer favoriteCount;
    private Integer commentCount;
    private Date createdAt;
}
