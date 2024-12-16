package com.example.jpa_relationn.service;

import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.jpa_relationn.model.Like;
import com.example.jpa_relationn.model.Post;
import com.example.jpa_relationn.model.RePost;
import com.example.jpa_relationn.model.User;
import com.example.jpa_relationn.repository.PostRepository;
import com.example.jpa_relationn.repository.RepostRepository;
import com.example.jpa_relationn.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RepostService {
        UserRepository userRepository;
        PostRepository postRepository;
        RepostRepository repostRepository;
        NotificationService notificationService;

        public RePost repostPost(Integer postId) {
                var context = SecurityContextHolder.getContext().getAuthentication();
                String name = context.getName();

                // Tìm User đang thực hiện hành động
                User user = userRepository.findByUsername(name)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                // Tìm bài viết cần repost/unrepost
                Post post = postRepository.findById(postId)
                                .orElseThrow(() -> new RuntimeException("Post not found"));

                // Kiểm tra nếu người dùng đã repost bài viết này
                Optional<RePost> existingRepost = repostRepository.findByUserAndPost(user, post);

                if (existingRepost.isPresent()) {
                        // Nếu đã repost, thực hiện unrepost
                        RePost rePost = existingRepost.get();

                        // Giảm giá trị repostCount (đảm bảo không giảm xuống dưới 0)
                        if (post.getRepostCount() > 0) {
                                post.setRepostCount(post.getRepostCount() - 1);
                        }

                        // Xóa liên kết giữa user, post và repost
                        user.removeRePost(rePost);
                        post.removeRePost(rePost);

                        // Đánh dấu bài viết chưa được repost
                        post.setReposted(false);

                        // Xóa repost khỏi cơ sở dữ liệu
                        repostRepository.delete(rePost);

                        return null; // Không trả về đối tượng RePost vì đã thực hiện unrepost
                } else {
                        // Nếu chưa repost, thực hiện repost
                        RePost rePost = new RePost();
                        user.addRePost(rePost);
                        post.addRePost(rePost);

                        // Tăng giá trị repostCount
                        post.setRepostCount(post.getRepostCount() + 1);

                        // Đánh dấu bài viết chưa được repost
                        post.setReposted(true);

                        // gửi thông báo chia sẻ
                        notificationService.notifyPostRepost(user, post);

                        // Lưu đối tượng RePost
                        return repostRepository.save(rePost);
                }
        }

        public boolean checkRepostStatus(Integer postId) {
                var context = SecurityContextHolder.getContext().getAuthentication();
                String name = context.getName();

                // Tìm User đang thực hiện hành động
                User user = userRepository.findByUsername(name)
                                .orElseThrow(() -> new RuntimeException("User not found"));
                Post post = postRepository.findById(postId)
                                .orElseThrow(() -> new RuntimeException("Post not found"));
                // Kiểm tra nếu có bản ghi trong bảng likes với userId và postId
                return repostRepository.existsByUserAndPost(user, post);
        }
}
