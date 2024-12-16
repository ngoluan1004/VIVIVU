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

import com.example.jpa_relationn.service.RepostService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reposts")
public class RepostController {

    @Autowired
    private final RepostService repostService;

    @PostMapping("/add/{postId}")
    public ResponseEntity<String> repostPost(@PathVariable Integer postId) {
        repostService.repostPost(postId);
        return ResponseEntity.ok("chia sẻ bài đăng thành công!");
    }

    @GetMapping("/status/{postId}")
    public ResponseEntity<Map<String, Boolean>> getRepostStatus(@PathVariable Integer postId) {
        try {
            // Kiểm tra trạng thái reposted
            boolean reposted = repostService.checkRepostStatus(postId);
            // Trả về response
            Map<String, Boolean> response = new HashMap<>();
            response.put("reposted", reposted);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // @PostMapping("/remove/{userId}/{postId}")
    // public ResponseEntity<String> unRepostPost(@PathVariable Integer userId,
    // @PathVariable Integer postId) {
    // repostService.unRepostPost(userId, postId);
    // return ResponseEntity.ok("Post unreposted successfully!");
    // }

    // @PostMapping("/add")
    // public ResponseEntity<String> repostPost(
    // @RequestParam Integer userId,
    // @RequestParam Integer postId) {
    // repostService.repostPost(userId, postId);
    // return ResponseEntity.ok("Post reposted successfully!");
    // }

    // @PostMapping("/remove")
    // public ResponseEntity<String> unRepostPost(
    // @RequestParam Integer userId,
    // @RequestParam Integer postId) {
    // repostService.unRepostPost(userId, postId);
    // return ResponseEntity.ok("Post unreposted successfully!");
    // }

}
