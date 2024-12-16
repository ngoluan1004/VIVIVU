package com.example.jpa_relationn.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.jpa_relationn.model.Like;
import com.example.jpa_relationn.model.Post;
import com.example.jpa_relationn.dto.request.UserCreationRequest;
import com.example.jpa_relationn.dto.request.UserUpdateRequest;
import com.example.jpa_relationn.dto.response.UserResponse;
import com.example.jpa_relationn.enums.Role;
import com.example.jpa_relationn.enums.Status;
import com.example.jpa_relationn.exception.AppException;
import com.example.jpa_relationn.exception.ErrorCode;
import com.example.jpa_relationn.mapper.UserMapper;
import com.example.jpa_relationn.model.User;
import com.example.jpa_relationn.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final UserMapper userMapper;
    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Value("${file.upload-dir}")
    private String uploadDir;

    // tạo user mới
    public User createUser(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        try {
            // Lưu backgroundImage
            String backgroundImageFileName = System.currentTimeMillis() + "_"
                    + request.getBackgroundImage().getOriginalFilename();
            String imgUrlFileName = System.currentTimeMillis() + "_"
                    + request.getImgUrl().getOriginalFilename();

            Path backgroundImagePath = Paths.get(uploadDir, backgroundImageFileName);
            Path imgUrlPath = Paths.get(uploadDir, imgUrlFileName);

            // Lưu tệp vào hệ thống
            Files.copy(request.getBackgroundImage().getInputStream(), backgroundImagePath);
            Files.copy(request.getImgUrl().getInputStream(), imgUrlPath);

            User user = new User();
            user.setFullName(request.getFullName());
            user.setUsername(request.getUsername());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setEmail(request.getEmail());
            user.setLocation(request.getLocation());
            user.setBackgroundImage(backgroundImagePath.toString());
            user.setImgUrl(imgUrlPath.toString());
            user.setBirthDate(request.getBirthDate());
            user.setRole(Role.USER);
            user.setStatus(Status.ACTIVE);

            return userRepository.save(user);
        } catch (IOException e) {
            throw new RuntimeException("Error saving file: " + e.getMessage());
        }
    }

    // cập nhật thông tin user
    public User updateUser(Integer userId, UserUpdateRequest request) {
        try {
            // Lưu backgroundImage
            String backgroundImageFileName = System.currentTimeMillis() + "_"
                    + request.getBackgroundImage().getOriginalFilename();
            String imgUrlFileName = System.currentTimeMillis() + "_"
                    + request.getImgUrl().getOriginalFilename();

            Path backgroundImagePath = Paths.get(uploadDir, backgroundImageFileName);
            Path imgUrlPath = Paths.get(uploadDir, imgUrlFileName);

            // Lưu tệp vào hệ thống
            Files.copy(request.getBackgroundImage().getInputStream(), backgroundImagePath);
            Files.copy(request.getImgUrl().getInputStream(), imgUrlPath);

            User user = userRepository.findById(userId).orElseThrow();
            user.setFullName(request.getFullName());
            user.setBio(request.getBio());
            user.setEmail(request.getEmail());
            user.setLocation(request.getLocation());
            user.setBackgroundImage(backgroundImagePath.toString());
            user.setImgUrl(imgUrlPath.toString());
            user.setBirthDate(request.getBirthDate());
            user.setRole(Role.USER);
            user.setStatus(Status.ACTIVE);

            return userRepository.save(user);
        } catch (IOException e) {
            throw new RuntimeException("Error saving file: " + e.getMessage());
        }
    }

    // lấy ra tất ra user
    public List<User> getUsers() {
        List<User> users = userRepository.findAll();
        // log.info("In method get user by id");
        List<User> lusers = users.stream().map(user -> {
            User userDTO = new User();
            userDTO.setUserId(user.getUserId());
            userDTO.setUsername(user.getUsername());
            userDTO.setEmail(user.getEmail());
            userDTO.setPassword(user.getPassword());
            userDTO.setFullName(user.getFullName());
            userDTO.setImgUrl("/images/" + Paths.get(user.getImgUrl()).getFileName().toString());
            userDTO.setBackgroundImage("/images/" + Paths.get(user.getBackgroundImage()).getFileName().toString());
            userDTO.setBio(user.getBio());
            userDTO.setBirthDate(user.getBirthDate());
            userDTO.setLocation(user.getLocation());
            userDTO.setRole(user.getRole());
            userDTO.setStatus(user.getStatus());
            userDTO.setCreatedAt(user.getCreatedAt());
            userDTO.setFollowingCount(user.getFollowings().size());
            userDTO.setFollowerCount(user.getFollowers().size());
            return userDTO;
        }).collect(Collectors.toList());
        return lusers;
    }

    // user.setFollowingCount();
    // @PostAuthorize("returnObject.username == authentication.name")
    public User getUserById(Integer id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("Post not found with id: " + id);
        }
        // log.info("In method get user by id");
        User user = userOptional.get();

        User userResponse = new User();
        userResponse.setUserId(user.getUserId());
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
        userResponse.setPassword(user.getPassword());
        userResponse.setFullName(user.getFullName());
        userResponse.setImgUrl("/images/" + Paths.get(user.getImgUrl()).getFileName().toString());
        userResponse.setBackgroundImage("/images/" + Paths.get(user.getBackgroundImage()).getFileName().toString());
        userResponse.setBio(user.getBio());
        userResponse.setBirthDate(user.getBirthDate());
        userResponse.setLocation(user.getLocation());
        userResponse.setRole(user.getRole());
        userResponse.setStatus(user.getStatus());
        userResponse.setCreatedAt(user.getCreatedAt());
        userResponse.setFollowingCount(user.getFollowings().size());
        userResponse.setFollowerCount(user.getFollowers().size());
        return userResponse;
    }

    // lấy ra thông tin user by username
    public List<User> getUsersByName(String username) {
        List<User> users = userRepository.findAllByUsername(username);
        if (users.isEmpty()) {
            throw new RuntimeException("Post not found with id: " + username);
        }
        // log.info("In method get user by id");
        List<User> lusers = users.stream().map(user -> {
            User userDTO = new User();
            userDTO.setUserId(user.getUserId());
            userDTO.setUsername(user.getUsername());
            userDTO.setEmail(user.getEmail());
            userDTO.setPassword(user.getPassword());
            userDTO.setFullName(user.getFullName());
            userDTO.setImgUrl("/images/" + Paths.get(user.getImgUrl()).getFileName().toString());
            userDTO.setBackgroundImage("/images/" + Paths.get(user.getBackgroundImage()).getFileName().toString());
            userDTO.setBio(user.getBio());
            userDTO.setBirthDate(user.getBirthDate());
            userDTO.setLocation(user.getLocation());
            userDTO.setRole(user.getRole());
            userDTO.setStatus(user.getStatus());
            userDTO.setCreatedAt(user.getCreatedAt());
            userDTO.setFollowingCount(user.getFollowings().size());
            userDTO.setFollowerCount(user.getFollowers().size());
            return userDTO;
        }).collect(Collectors.toList());
        return lusers;
    }

    // lấy ra 10 User mới nhất
    public List<User> get10NewestUser() {
        List<User> lUsers = userRepository.findTop10ByOrderByUserIdDesc();
        List<User> userDTOs = lUsers.stream().map(user -> {
            User userDTO = new User();
            userDTO.setUserId(user.getUserId());
            userDTO.setUsername(user.getUsername());
            userDTO.setEmail(user.getEmail());
            userDTO.setPassword(user.getPassword());
            userDTO.setFullName(user.getFullName());
            userDTO.setImgUrl("/images/" + Paths.get(user.getImgUrl()).getFileName().toString());
            userDTO.setBackgroundImage("/images/" + Paths.get(user.getBackgroundImage()).getFileName().toString());
            userDTO.setBio(user.getBio());
            userDTO.setBirthDate(user.getBirthDate());
            userDTO.setLocation(user.getLocation());
            userDTO.setRole(user.getRole());
            userDTO.setStatus(user.getStatus());
            userDTO.setCreatedAt(user.getCreatedAt());
            userDTO.setFollowingCount(user.getFollowings().size());
            userDTO.setFollowerCount(user.getFollowers().size());
            return userDTO;
        }).collect(Collectors.toList());
        return userDTOs;
    }

    // lấy thông tin của user hiện tại
    public User getMyInfo() {
        var context = SecurityContextHolder.getContext().getAuthentication();
        String name = context.getName();

        User user = userRepository.findByUsername(name).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return user;
    }

    // Khóa user
    public User banUser(Integer userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }

        User user = optionalUser.get();
        if (user.getStatus() == Status.ACTIVE) {
            user.setStatus(Status.BANNED);
        } else if (user.getStatus() == Status.BANNED) {
            user.setStatus(Status.ACTIVE);
        } else {
            throw new IllegalStateException("User status cannot be toggled from " + user.getStatus());
        }

        return userRepository.save(user);
    }

    // kiểm tra trạng thái của user có phải active không?
    public boolean isUserActive(Integer userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }
        User user = optionalUser.get();
        return user.getStatus() == Status.ACTIVE;
    }

    // xóa user
    public void deleteUser(Integer userId) {
        userRepository.deleteById(userId);
    }
}
