package com.example.jpa_relationn.service;

import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.jpa_relationn.model.Favorite;
import com.example.jpa_relationn.model.Like;
import com.example.jpa_relationn.model.Post;
import com.example.jpa_relationn.model.User;
import com.example.jpa_relationn.repository.LikeRepository;
import com.example.jpa_relationn.repository.PostRepository;
import com.example.jpa_relationn.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LikeService {
        UserRepository userRepository;
        PostRepository postRepository;
        LikeRepository likeRepository;
        NotificationService notificationService;

        public Like likePost(Integer postId) {
                var context = SecurityContextHolder.getContext().getAuthentication();
                String name = context.getName();

                // Tìm User đang thực hiện hành động
                User user = userRepository.findByUsername(name)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                // Tìm bài viết cần Like/Unlike
                Post post = postRepository.findById(postId)
                                .orElseThrow(() -> new RuntimeException("Post not found"));

                // Kiểm tra nếu người dùng đã Like bài viết này trước đó
                Optional<Like> existingLike = likeRepository.findByUserAndPost(user, post);

                if (existingLike.isPresent()) {
                        // Nếu đã Like, thực hiện Unlike
                        Like like = existingLike.get();

                        // Giảm giá trị likesCount (đảm bảo không giảm xuống dưới 0)
                        if (post.getLikesCount() > 0) {
                                post.setLikesCount(post.getLikesCount() - 1);
                        }

                        // Đánh dấu bài viết chưa được Like
                        post.setLiked(false);

                        // Xóa liên kết giữa user, post và like
                        user.removeLike(like);
                        post.removeLike(like);

                        // Xóa like khỏi cơ sở dữ liệu
                        likeRepository.delete(like);

                        return null; // Không trả về đối tượng Like vì là Unlike
                }
                // Nếu chưa Like, thực hiện Like
                Like like = new Like();
                user.addLike(like);
                post.addLike(like);

                // Tăng giá trị likesCount
                post.setLikesCount(post.getLikesCount() + 1);

                // Đánh dấu bài viết đã được Like
                post.setLiked(true);

                // gửi thông báo like
                notificationService.notifyPostLiked(user, post);

                // Lưu đối tượng Like
                return likeRepository.save(like);

        }

        public boolean checkLikeStatus(Integer postId) {
                var context = SecurityContextHolder.getContext().getAuthentication();
                String name = context.getName();

                // Tìm User đang thực hiện hành động
                User user = userRepository.findByUsername(name)
                                .orElseThrow(() -> new RuntimeException("User not found"));
                Post post = postRepository.findById(postId)
                                .orElseThrow(() -> new RuntimeException("Post not found"));
                // Kiểm tra nếu có bản ghi trong bảng likes với userId và postId
                return likeRepository.existsByUserAndPost(user, post);
        }
}
