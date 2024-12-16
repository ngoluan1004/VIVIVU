package com.example.jpa_relationn.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.example.jpa_relationn.dto.request.PostUpdataRequest;
import com.example.jpa_relationn.dto.response.PostResponse;
import com.example.jpa_relationn.model.Post;

@Mapper(componentModel = "spring")
public interface PostMapper {
  // Post toPost(PostCreatationRequest request);

  PostResponse toPostResponse(Post post);

  List<PostResponse> toPostResponse1(List<Post> posts);

  void updatePost(@MappingTarget Post post, PostUpdataRequest request);
}
