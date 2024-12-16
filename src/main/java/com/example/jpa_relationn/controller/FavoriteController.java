package com.example.jpa_relationn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.jpa_relationn.service.FavoriteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/favorites")
public class FavoriteController {
    @Autowired
    private final FavoriteService favoriteService;

    @PostMapping("/add/{postId}")
    public ResponseEntity<String> favoritePost(@PathVariable Integer postId) {
        favoriteService.favoritePost(postId);
        return ResponseEntity.ok("chia sẻ bài thành công!");
    }

    @GetMapping("/status/{postId}")
    public ResponseEntity<Map<String, Boolean>> getRepostStatus(@PathVariable Integer postId) {
        try {
            // Kiểm tra trạng thái reposted
            boolean favorited = favoriteService.checkFavoriteStatus(postId);
            // Trả về response
            Map<String, Boolean> response = new HashMap<>();
            response.put("favorited", favorited);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // @PostMapping("/add/{userId}/{postId}")
    // public ResponseEntity<String> favoritePost(@PathVariable Integer userId,
    // @PathVariable Integer postId) {
    // favoriteService.favoritePost(userId, postId);
    // return ResponseEntity.ok("Post faved successfully!");
    // }

    // @PostMapping("/remove/{userId}/{postId}")
    // public ResponseEntity<String> unFavoritePost(@PathVariable Integer userId,
    // @PathVariable Integer postId) {
    // favoriteService.unFavoritePost(userId, postId);
    // return ResponseEntity.ok("Post unfaved successfully!");
    // }

    // @PostMapping("/add")
    // public ResponseEntity<String> favoritePost(
    // @RequestParam Integer userId,
    // @RequestParam Integer postId) {
    // favoriteService.favoritePost(userId, postId);
    // return ResponseEntity.ok("Post faved successfully!");
    // }

    // @PostMapping("/remove")
    // public ResponseEntity<String> unFavoritePost(
    // @RequestParam Integer userId,
    // @RequestParam Integer postId) {
    // favoriteService.unFavoritePost(userId, postId);
    // return ResponseEntity.ok("Post unfaved successfully!");
    // }
}
