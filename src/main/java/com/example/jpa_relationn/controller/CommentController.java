package com.example.jpa_relationn.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.jpa_relationn.dto.request.UpdateComment;
import com.example.jpa_relationn.model.Comment;
import com.example.jpa_relationn.service.CommentService;
import com.example.jpa_relationn.service.LikeService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentController {

    CommentService commentService;

    @PostMapping("/add/{postId}")
    public ResponseEntity<Comment> addComment(@PathVariable("postId") Integer postId, @RequestBody Comment comment) {
        Comment savedComment = commentService.addComment(postId, comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedComment);
    }

    @DeleteMapping("/{commentId}")
    public String deleteComment(@PathVariable("commentId") Integer commentId) {
        commentService.deleteComment(commentId);
        return "Xóa bình luận thành công";
    }

    @GetMapping("/getallcmt/{postId}")
    public ResponseEntity<?> getMethodName(@PathVariable("postId") Integer postId) {
        return new ResponseEntity<>(commentService.getAllCommentsByPostId(postId), HttpStatus.OK);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable("commentId") Integer commentId,
            @RequestBody UpdateComment comment) {
        Comment updateComment = commentService.updateComment(commentId, comment);
        return new ResponseEntity<Comment>(updateComment, HttpStatus.OK);
    }

}
