package com.example.jpa_relationn.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.jpa_relationn.service.LikeService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequiredArgsConstructor
@RequestMapping("/likes")
public class LikeController {
    @Autowired
    private final LikeService likeService;

    @PostMapping("/add/{postId}")
    public ResponseEntity<String> likePost(@PathVariable Integer postId) {
        likeService.likePost(postId);
        return ResponseEntity.ok("Đã thích bài viết!");
    }

    @GetMapping("/status/{postId}")
    public ResponseEntity<Map<String, Boolean>> getLikeStatus(@PathVariable Integer postId) {
        try {
            // Lấy userId từ JWT
            // Kiểm tra trạng thái like
            boolean liked = likeService.checkLikeStatus(postId);
            // Trả về response
            Map<String, Boolean> response = new HashMap<>();
            response.put("liked", liked);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // @PostMapping("/remove/{postId}")
    // public ResponseEntity<String> unlikePost(@PathVariable Integer postId) {
    // likeService.unlikePost(postId);
    // return ResponseEntity.ok("Post unliked successfully!");
    // }
    // @PostMapping("/add")
    // public ResponseEntity<String> likePost(
    // @RequestParam Integer userId,
    // @RequestParam Integer postId) {
    // likeService.likePost(userId, postId);
    // return ResponseEntity.ok("Post liked successfully!");
    // }

    // @PostMapping("/remove")
    // public ResponseEntity<String> unlikePost(
    // @RequestParam Integer userId,
    // @RequestParam Integer postId) {
    // likeService.unlikePost(userId, postId);
    // return ResponseEntity.ok("Post unliked successfully!");
    // }
}
