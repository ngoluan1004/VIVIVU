package com.example.jpa_relationn.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.jpa_relationn.model.User;
import com.example.jpa_relationn.service.FollowService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/follows")
public class FollowController {
    @Autowired
    private final FollowService followService;

    @PostMapping("/follow/{followingId}")
    public ResponseEntity<String> followUser(@PathVariable Integer followingId) {
        followService.followUser(followingId);
        return ResponseEntity.ok("theo dõi thành công!");
    }

    @GetMapping("/status/{userId}")
    public ResponseEntity<Map<String, Boolean>> getLikeStatus(@PathVariable Integer userId) {
        try {
            // Lấy userId từ JWT
            // Kiểm tra trạng thái like
            boolean followed = followService.checkFollowStatus(userId);
            // Trả về response
            Map<String, Boolean> response = new HashMap<>();
            response.put("followed", followed);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // API để lấy danh sách các user mà user A đang theo dõi
    @GetMapping("/{followerId}/followers")
    public ResponseEntity<List<User>> getUsersFollowing(@PathVariable Integer followerId) {
        List<User> usersFollowing = followService.getUsersFollowing(followerId);
        return ResponseEntity.ok(usersFollowing);
    }

    // API để lấy danh sách các user đang follow user A
    @GetMapping("/{followingId}/followings")
    public ResponseEntity<List<User>> getUsersFollower(@PathVariable Integer followingId) {
        List<User> usersFollower = followService.getUsersFollower(followingId);
        return ResponseEntity.ok(usersFollower);
    }
}
