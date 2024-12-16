package com.example.jpa_relationn.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.jpa_relationn.dto.request.UserCreationRequest;
import com.example.jpa_relationn.dto.request.UserUpdateRequest;
import com.example.jpa_relationn.dto.response.ApiResponse;
import com.example.jpa_relationn.dto.response.UserResponse;
import com.example.jpa_relationn.model.User;
import com.example.jpa_relationn.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    @Autowired
    private final UserService userService;

    // lấy danh sách user
    @GetMapping("/list")
    public ResponseEntity<?> getUsers() {
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }

    // tạo user
    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(@ModelAttribute UserCreationRequest request) {
        return new ResponseEntity<>(userService.createUser(request), HttpStatus.CREATED);
    }

    // get user by ID
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable("userId") Integer id) {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    // lấy thông tin user hiện tại
    @GetMapping("/myInfo")
    public ResponseEntity<?> getMyInfo() {
        return new ResponseEntity<>(userService.getMyInfo(), HttpStatus.OK);
    }

    // sửa thông tin user
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Integer userId,
            @ModelAttribute UserUpdateRequest userUpdateRequest) {
        return new ResponseEntity<>(userService.updateUser(userId, userUpdateRequest), HttpStatus.OK);
    }

    // xóa user
    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable Integer userId) {
        userService.deleteUser(userId);
        return "xóa user thành công";
    }

    // tìm kiếm user by username
    @GetMapping("/search")
    public ResponseEntity<List<User>> findUserByUsername(@RequestParam String username) {
        List<User> users = userService.getUsersByName(username);
        return ResponseEntity.ok(users);
    }

    // lấy ra 10 bài đăng gần nhất
    @GetMapping("/newest")
    public ResponseEntity<List<User>> get10NewestUser() {
        List<User> lUsers = userService.get10NewestUser();
        return ResponseEntity.ok(lUsers);
    }

    @PatchMapping("/ban/{userId}")
    public ResponseEntity<String> banUser(@PathVariable Integer userId) {
        try {
            User updatedUser = userService.banUser(userId);
            String newStatus = updatedUser.getStatus().name();
            return ResponseEntity
                    .ok("Trạng thái của " + updatedUser.getUsername() + " đã được chuyển thành " + newStatus + ".");
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{userId}/is-active")
    public ResponseEntity<Map<String, Boolean>> checkIfUserIsActive(@PathVariable Integer userId) {
        try {
            // Lấy userId từ JWT
            // Kiểm tra trạng thái like
            boolean isActive = userService.isUserActive(userId);
            // Trả về response
            Map<String, Boolean> response = new HashMap<>();
            response.put("isActive", isActive);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
