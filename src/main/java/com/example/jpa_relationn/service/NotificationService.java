package com.example.jpa_relationn.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.jpa_relationn.enums.NotidicationType;
import com.example.jpa_relationn.exception.AppException;
import com.example.jpa_relationn.exception.ErrorCode;
import com.example.jpa_relationn.model.Comment;
import com.example.jpa_relationn.model.Notification;
import com.example.jpa_relationn.model.Post;
import com.example.jpa_relationn.model.User;
import com.example.jpa_relationn.repository.NotificationRepository;
import com.example.jpa_relationn.repository.UserRepository;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    public void createNotification(User targetUser, String content, NotidicationType type, Post post, Comment comment) {
        Notification notification = new Notification();
        notification.setUser(targetUser);
        notification.setContent(content);
        notification.setType(type);
        notification.setIsRead(false);
        notification.setRelatedPost(post);
        notification.setRelatedComment(comment);
        // notification.setCreatedAt(new Date());

        notificationRepository.save(notification);
    }

    public List<Notification> getAllNotification() {
        // Lấy thông tin người dùng đang đăng nhập
        var context = SecurityContextHolder.getContext().getAuthentication();
        String name = context.getName();

        // Tìm người dùng
        User user = userRepository.findByUsername(name).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Lấy danh sách thông báo của người dùng
        return notificationRepository.findAllByUserOrderByNotificationIdDesc(user);
    }

    // Ví dụ gửi thông báo khi một bài viết được like
    public void notifyPostLiked(User liker, Post post) {
        User postOwner = post.getUser();
        if (!liker.equals(postOwner)) { // Không gửi thông báo cho chính người like
            String content = liker.getUsername() + " liked your post: " + post.getTitle();
            createNotification(postOwner, content, NotidicationType.LIKE, post, null);
        }
    }

    // Ví dụ gửi thông báo khi có comment mới
    public void notifyComment(User commenter, Post post, Comment comment) {
        User postOwner = post.getUser();
        if (!commenter.equals(postOwner)) { // Không gửi thông báo cho chính người bình luận
            String content = commenter.getUsername() + " commented on your post: " + post.getTitle();
            createNotification(postOwner, content, NotidicationType.COMMENT, post, comment);
        }
    }

    // Ví dụ gửi thông báo khi một bài viết được repost
    public void notifyPostRepost(User reposter, Post post) {
        User postOwner = post.getUser();
        if (!reposter.equals(postOwner)) { // Không gửi thông báo cho chính người repost
            String content = reposter.getUsername() + " reposted your post: " + post.getTitle();
            createNotification(postOwner, content, NotidicationType.REPORT, post, null);
        }
    }

    // Ví dụ gửi thông báo khi một bài viết được favorite
    public void notifyPostFavorite(User favoriter, Post post) {
        User postOwner = post.getUser();
        if (!favoriter.equals(postOwner)) { // Không gửi thông báo cho chính người yêu thích
            String content = favoriter.getUsername() + " favorited your post: " + post.getTitle();
            createNotification(postOwner, content, NotidicationType.LIKE, post, null);
        }
    }

    // ví dụ gửi thông báo khi theo dõi 1 user
    public void notifyFollow(User follower, User followee) {
        if (!follower.equals(followee)) { // Không gửi thông báo cho chính người follow
            String content = follower.getUsername() + " started following you.";
            createNotification(followee, content, NotidicationType.FOLLOW, null, null);
        }
    }

}
