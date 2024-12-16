package com.example.jpa_relationn.service;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.jpa_relationn.dto.request.UpdateComment;
import com.example.jpa_relationn.enums.CommentStatus;
import com.example.jpa_relationn.model.Comment;
import com.example.jpa_relationn.model.Post;
import com.example.jpa_relationn.model.User;
import com.example.jpa_relationn.repository.CommentRepository;
import com.example.jpa_relationn.repository.PostRepository;
import com.example.jpa_relationn.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentService {
    UserRepository userRepository;
    CommentRepository commentRepository;
    PostRepository postRepository;
    NotificationService notificationService;

    public Comment addComment(Integer postId, Comment c) {
        var context = SecurityContextHolder.getContext().getAuthentication();
        String name = context.getName();
        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Comment comment = new Comment();
        comment.setContent(c.getContent());
        comment.setStatus(CommentStatus.VISIBLE);
        user.addComment(comment);
        post.addComment(comment);
        post.setCommentCount(post.getCommentCount() + 1);

        notificationService.notifyComment(user, post, comment);

        return commentRepository.save(comment);
    }

    public void deleteComment(Integer commentId) {
        // Tìm comment
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(name).orElseThrow(() -> new RuntimeException("User not found"));
        // Kiểm tra quyền xóa (nếu cần)
        if (!comment.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("You are not allowed to delete this comment");
        }

        // Xóa bình luận
        commentRepository.delete(comment);
    }

    public List<Comment> getAllCommentsByPostId(Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        return post.getAllComments();
    }

    public Comment updateComment(Integer commentId, UpdateComment updateComment) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        comment.setContent(updateComment.getContent());

        return commentRepository.save(comment);
    }
}
