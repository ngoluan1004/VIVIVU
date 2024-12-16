package com.example.jpa_relationn.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.jpa_relationn.dto.request.PostCreatationRequest;
import com.example.jpa_relationn.dto.request.PostRequest;
import com.example.jpa_relationn.dto.request.PostUpdataRequest;
import com.example.jpa_relationn.dto.response.PostResponse;
import com.example.jpa_relationn.enums.PostStatus;
import com.example.jpa_relationn.exception.AppException;
import com.example.jpa_relationn.exception.ErrorCode;
import com.example.jpa_relationn.mapper.PostMapper;
import com.example.jpa_relationn.model.Post;
import com.example.jpa_relationn.model.User;
import com.example.jpa_relationn.model.Image;
import com.example.jpa_relationn.model.Like;
import com.example.jpa_relationn.repository.PostRepository;
import com.example.jpa_relationn.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final PostRepository postRepository;
    @Autowired
    private final PostMapper postMapper;

    @Value("${file.upload-dir}")
    private String uploadDir;

    // tạo bài đăng
    public Post createPost(PostCreatationRequest postDTO) {
        var context = SecurityContextHolder.getContext().getAuthentication();
        String name = context.getName();
        User user = userRepository.findByUsername(name).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED));

        try {
            // Lưu ảnh vào thư mục
            String fileName = System.currentTimeMillis() + "_" + postDTO.getImage().getOriginalFilename();
            // Tạo đường dẫn lưu tệp.
            Path imagePath = Paths.get(uploadDir, fileName);
            // Lưu tệp tin vào máy chủ.
            Files.copy(postDTO.getImage().getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);

            Post post = new Post();
            post.setTitle(postDTO.getTitle());
            post.setContent(postDTO.getContent());
            post.setAddress(postDTO.getAddress());
            post.setCategory(postDTO.getCategory());
            post.setStatus(PostStatus.PUBLISHED);
            post.setLikesCount(post.getLikes().size());
            post.setRepostCount(post.getReports().size());
            post.setFavoriteCount(post.getFavorites().size());
            post.setCommentCount(post.getComments().size());
            user.addPost(post);

            post.setImage(imagePath.toString());

            return postRepository.save(post);
        } catch (IOException e) {
            throw new RuntimeException("Error saving file: " + e.getMessage());
        }
    }

    // lấy thông tin bài đăng qua Id
    public Post getPostById(Integer id) {
        Optional<Post> postOptional = postRepository.findById(id);
        if (postOptional.isEmpty()) {
            throw new RuntimeException("Post not found with id: " + id);
        }
        Post post = postOptional.get();

        // Map Post sang PostDTO
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
    }

    // lấy ra tất cả bài viết
    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }

    // lấy ra 10 bài post mới nhất
    public List<Post> get10NewestPost() {
        List<Post> lPosts = postRepository.findTop10ByOrderByPostIdDesc();
        return lPosts;
    }

    // tìm kiếm theo tên bài đăng
    public List<Post> findPostsByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            return new ArrayList<>(); // Trả về danh sách rỗng
        }
        return postRepository.searchByTitle(title);
    }

    // tìm kiếm theo tên chủ đề
    public List<Post> findPostsByCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            return new ArrayList<>(); // Trả về danh sách rỗng
        }
        return postRepository.searchByCategory(category);
    }

    // lấy ra bài đăng của user bất kì
    public List<Post> getUserPost(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow();
        List<Post> list = user.getListPost();
        return list;
    }

    // sửa bài đăng
    public Post updatePost(Integer postId, PostUpdataRequest postUpdataRequest) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED));

        post.setTitle(postUpdataRequest.getTitle());
        post.setContent(postUpdataRequest.getContent());
        post.setAddress(postUpdataRequest.getAddress());
        post.setCategory(postUpdataRequest.getCategory());

        postRepository.save(post);

        Post post2 = getPostById(post.getPostId());

        return post2;
    }

    // lấy ra bài đăng của tôi
    public List<Post> getMyPosts() {
        var context = SecurityContextHolder.getContext().getAuthentication();
        String name = context.getName();

        User user = userRepository.findByUsername(name).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED));
        List<Post> lPosts = user.getListPost();

        return lPosts;
    }

    // lấy ra các bài đăng User đã Like
    public List<Post> getPostsByLike(Integer userId) {
        return postRepository.findAllPostsLikedByUser(userId);
    }

    // lấy ra các bài đăng User đã đăng lại
    public List<Post> getPostsByRepost(Integer userId) {
        return postRepository.findAllPostsRepostedByUser(userId);
    }

    // lấy ra các bài đăng User đã thêm vào mục yêu thích
    public List<Post> getPostsByFavorite(Integer userId) {
        return postRepository.findAllPostsFavoritedByUser(userId);
    }

    // xóa bài đăng
    public void deleteById(Integer postId) {
        postRepository.deleteById(postId);
    }

    public Post hidePost(Integer postId) {
        // Tìm bài viết theo postId
        Post post = postRepository.findById(postId)
                .orElseThrow();

        // Chỉ cho phép ẩn bài viết nếu trạng thái hiện tại là PUBLISHED
        if (post.getStatus() != PostStatus.PUBLISHED) {
            throw new IllegalStateException("Only PUBLISHED posts can be hidden.");
        }

        // Chuyển trạng thái thành HIDDEN
        post.setStatus(PostStatus.HIDDEN);

        // Lưu lại bài viết đã cập nhật
        return postRepository.save(post);
    }

}
