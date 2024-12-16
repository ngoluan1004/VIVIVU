package com.example.jpa_relationn.controller;

import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jpa_relationn.dto.request.PostCreatationRequest;
import com.example.jpa_relationn.dto.request.PostUpdataRequest;
import com.example.jpa_relationn.model.Post;
import com.example.jpa_relationn.service.PostService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    @Autowired
    private final PostService postService;

    // create post
    @PostMapping("/create")
    public Post createPost(@ModelAttribute PostCreatationRequest postDTO) {
        Post p = postService.createPost(postDTO);
        Post postResponse = postService.getPostById(p.getPostId());
        return postResponse;
    }

    // get post by id
    @GetMapping("/{id}")
    public Post getPostById(@PathVariable Integer id) {
        return postService.getPostById(id);
    }

    // lấy ra tất cả bài đăng
    @GetMapping("/posts")
    public List<Post> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        List<Post> postDTOs = posts.stream().map(post -> {
            Post postDTO = new Post();
            postDTO.setPostId(post.getPostId());
            postDTO.setStatus(post.getStatus());
            postDTO.setTitle(post.getTitle());
            postDTO.setContent(post.getContent());
            postDTO.setCategory(post.getCategory());
            postDTO.setAddress(post.getAddress());
            postDTO.setLikesCount(post.getLikes().size());
            postDTO.setRepostCount(post.getReposts().size());
            postDTO.setFavoriteCount(post.getFavorites().size());
            postDTO.setCommentCount(post.getComments().size());
            postDTO.setUser(post.getUser());
            postDTO.setCreatedAt(post.getCreatedAt());
            postDTO.setImage("/images/" + Paths.get(post.getImage()).getFileName().toString());
            return postDTO;
        }).collect(Collectors.toList());
        return postDTOs;
    }

    // lấy ra 10 bài đăng gần nhất
    @GetMapping("/newest")
    public List<Post> get10LatestPosts() {
        List<Post> posts = postService.get10NewestPost();
        List<Post> postDTOs = posts.stream().map(post -> {
            Post postDTO = new Post();
            postDTO.setPostId(post.getPostId());
            postDTO.setStatus(post.getStatus());
            postDTO.setTitle(post.getTitle());
            postDTO.setContent(post.getContent());
            postDTO.setCategory(post.getCategory());
            postDTO.setAddress(post.getAddress());
            postDTO.setLikesCount(post.getLikes().size());
            postDTO.setRepostCount(post.getReposts().size());
            postDTO.setFavoriteCount(post.getFavorites().size());
            postDTO.setCommentCount(post.getComments().size());
            postDTO.setUser(post.getUser());
            postDTO.setCreatedAt(post.getCreatedAt());
            postDTO.setImage("/images/" + Paths.get(post.getImage()).getFileName().toString());
            return postDTO;
        }).collect(Collectors.toList());
        return postDTOs;
    }

    // tìm kiếm bài đăng qua tiêu đề
    @GetMapping("/search")
    public List<Post> findPostsByTitle(@RequestParam(name = "title", required = false) String title) {
        List<Post> posts = postService.findPostsByTitle(title);
        List<Post> postDTOs = posts.stream().map(post -> {
            Post postDTO = new Post();
            postDTO.setPostId(post.getPostId());
            postDTO.setStatus(post.getStatus());
            postDTO.setTitle(post.getTitle());
            postDTO.setContent(post.getContent());
            postDTO.setCategory(post.getCategory());
            postDTO.setAddress(post.getAddress());
            postDTO.setLikesCount(post.getLikes().size());
            postDTO.setRepostCount(post.getReposts().size());
            postDTO.setFavoriteCount(post.getFavorites().size());
            postDTO.setCommentCount(post.getComments().size());
            postDTO.setUser(post.getUser());
            postDTO.setCreatedAt(post.getCreatedAt());
            postDTO.setImage("/images/" + Paths.get(post.getImage()).getFileName().toString());
            return postDTO;
        }).collect(Collectors.toList());
        return postDTOs;
    }

    // tìm kiếm bài đăng qua thể loại
    @GetMapping("/VietNamcategory")
    public List<Post> findPostsByVietNamCategory() {
        String category = "VIETNAM";
        List<Post> posts = postService.findPostsByCategory(category);
        List<Post> postDTOs = posts.stream().map(post -> {
            Post postDTO = new Post();
            postDTO.setPostId(post.getPostId());
            postDTO.setStatus(post.getStatus());
            postDTO.setTitle(post.getTitle());
            postDTO.setContent(post.getContent());
            postDTO.setCategory(post.getCategory());
            postDTO.setAddress(post.getAddress());
            postDTO.setLikesCount(post.getLikes().size());
            postDTO.setRepostCount(post.getReposts().size());
            postDTO.setFavoriteCount(post.getFavorites().size());
            postDTO.setCommentCount(post.getComments().size());
            postDTO.setUser(post.getUser());
            postDTO.setCreatedAt(post.getCreatedAt());
            postDTO.setImage("/images/" + Paths.get(post.getImage()).getFileName().toString());
            return postDTO;
        }).collect(Collectors.toList());
        return postDTOs;
    }

    // tìm kiếm bài đăng qua thể loại
    @GetMapping("/Thegioicategory")
    public List<Post> findPostsByTheGioiCategory() {
        String category = "THEGIOI";
        List<Post> posts = postService.findPostsByCategory(category);
        List<Post> postDTOs = posts.stream().map(post -> {
            Post postDTO = new Post();
            postDTO.setPostId(post.getPostId());
            postDTO.setStatus(post.getStatus());
            postDTO.setTitle(post.getTitle());
            postDTO.setContent(post.getContent());
            postDTO.setCategory(post.getCategory());
            postDTO.setAddress(post.getAddress());
            postDTO.setLikesCount(post.getLikes().size());
            postDTO.setRepostCount(post.getReposts().size());
            postDTO.setFavoriteCount(post.getFavorites().size());
            postDTO.setCommentCount(post.getComments().size());
            postDTO.setUser(post.getUser());
            postDTO.setCreatedAt(post.getCreatedAt());
            postDTO.setImage("/images/" + Paths.get(post.getImage()).getFileName().toString());
            return postDTO;
        }).collect(Collectors.toList());
        return postDTOs;
    }

    // lấy hết danh sách bài đăng của user
    @GetMapping("/users/{userId}")
    public List<Post> getUserPost(@PathVariable("userId") Integer userId) {
        List<Post> posts = postService.getUserPost(userId);
        List<Post> postDTOs = posts.stream().map(post -> {
            Post postDTO = new Post();
            postDTO.setPostId(post.getPostId());
            postDTO.setStatus(post.getStatus());
            postDTO.setTitle(post.getTitle());
            postDTO.setContent(post.getContent());
            postDTO.setCategory(post.getCategory());
            postDTO.setAddress(post.getAddress());
            postDTO.setLikesCount(post.getLikes().size());
            postDTO.setRepostCount(post.getReposts().size());
            postDTO.setFavoriteCount(post.getFavorites().size());
            postDTO.setCommentCount(post.getComments().size());
            postDTO.setUser(post.getUser());
            postDTO.setCreatedAt(post.getCreatedAt());
            postDTO.setImage("/images/" + Paths.get(post.getImage()).getFileName().toString());
            return postDTO;
        }).collect(Collectors.toList());
        return postDTOs;
    }

    // @GetMapping("/newest")
    // public ResponseEntity<?> get10NewestPost() {
    // return new ResponseEntity<>(postService.get10NewestPost(), HttpStatus.OK);
    // }

    // cập nhật bài đăng
    @PutMapping("{postId}")
    public Post updatePost(@PathVariable("postId") Integer postId,
            @RequestBody PostUpdataRequest request) {
        Post p = postService.updatePost(postId, request);
        return p;
    }

    // xóa bài đăng
    @DeleteMapping("{postId}")
    public String deletePost(@PathVariable("postId") Integer postId) {
        postService.deleteById(postId);
        return "xóa thành công";
    }

    // lấy ra danh sách user đã like
    @GetMapping("/like/{userId}")
    public List<Post> getPostByLike(@PathVariable("userId") Integer userId) {
        List<Post> posts = postService.getPostsByLike(userId);
        List<Post> postDTOs = posts.stream().map(post -> {
            Post postDTO = new Post();
            postDTO.setPostId(post.getPostId());
            postDTO.setStatus(post.getStatus());
            postDTO.setTitle(post.getTitle());
            postDTO.setContent(post.getContent());
            postDTO.setCategory(post.getCategory());
            postDTO.setAddress(post.getAddress());
            postDTO.setLikesCount(post.getLikes().size());
            postDTO.setRepostCount(post.getReposts().size());
            postDTO.setFavoriteCount(post.getFavorites().size());
            postDTO.setCommentCount(post.getComments().size());
            postDTO.setUser(post.getUser());
            postDTO.setCreatedAt(post.getCreatedAt());
            postDTO.setImage("/images/" + Paths.get(post.getImage()).getFileName().toString());
            return postDTO;
        }).collect(Collectors.toList());
        return postDTOs;
    }

    // lấy ra danh sách user đã repost
    @GetMapping("/repost/{userId}")
    public List<Post> getPostsByRepost(@PathVariable("userId") Integer userId) {
        List<Post> posts = postService.getPostsByRepost(userId);
        List<Post> postDTOs = posts.stream().map(post -> {
            Post postDTO = new Post();
            postDTO.setPostId(post.getPostId());
            postDTO.setStatus(post.getStatus());
            postDTO.setTitle(post.getTitle());
            postDTO.setContent(post.getContent());
            postDTO.setCategory(post.getCategory());
            postDTO.setAddress(post.getAddress());
            postDTO.setLikesCount(post.getLikes().size());
            postDTO.setRepostCount(post.getReposts().size());
            postDTO.setFavoriteCount(post.getFavorites().size());
            postDTO.setCommentCount(post.getComments().size());
            postDTO.setUser(post.getUser());
            postDTO.setCreatedAt(post.getCreatedAt());
            postDTO.setImage("/images/" + Paths.get(post.getImage()).getFileName().toString());
            return postDTO;
        }).collect(Collectors.toList());
        return postDTOs;
    }

    // lấy ra danh sách user đã favorite
    @GetMapping("/favorite/{userId}")
    public List<Post> getPostsByFavorite(@PathVariable("userId") Integer userId) {
        List<Post> posts = postService.getPostsByFavorite(userId);
        List<Post> postDTOs = posts.stream().map(post -> {
            Post postDTO = new Post();
            postDTO.setPostId(post.getPostId());
            postDTO.setStatus(post.getStatus());
            postDTO.setTitle(post.getTitle());
            postDTO.setContent(post.getContent());
            postDTO.setCategory(post.getCategory());
            postDTO.setAddress(post.getAddress());
            postDTO.setLikesCount(post.getLikes().size());
            postDTO.setRepostCount(post.getReposts().size());
            postDTO.setFavoriteCount(post.getFavorites().size());
            postDTO.setCommentCount(post.getComments().size());
            postDTO.setUser(post.getUser());
            postDTO.setCreatedAt(post.getCreatedAt());
            postDTO.setImage("/images/" + Paths.get(post.getImage()).getFileName().toString());
            return postDTO;
        }).collect(Collectors.toList());
        return postDTOs;
    }

    // ẩn bài đăng
    @PutMapping("/{postId}/hide")
    public ResponseEntity<Post> hidePost(@PathVariable Integer postId) {
        try {
            Post updatedPost = postService.hidePost(postId);
            return ResponseEntity.ok(updatedPost);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
