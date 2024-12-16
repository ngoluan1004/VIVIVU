package com.example.jpa_relationn.service;

import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.jpa_relationn.model.Favorite;
import com.example.jpa_relationn.model.Like;
import com.example.jpa_relationn.model.Post;
import com.example.jpa_relationn.model.User;
import com.example.jpa_relationn.repository.FavoriteRepository;
import com.example.jpa_relationn.repository.PostRepository;
import com.example.jpa_relationn.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FavoriteService {
        UserRepository userRepository;
        PostRepository postRepository;
        FavoriteRepository favoriteRepository;
        NotificationService notificationService;

        public Favorite favoritePost(Integer postId) {
                // Lấy thông tin người dùng hiện tại từ SecurityContext
                var context = SecurityContextHolder.getContext().getAuthentication();
                String name = context.getName();

                // Tìm User đang thực hiện hành động
                User user = userRepository.findByUsername(name)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                // Tìm bài viết cần favorite/unfavorite
                Post post = postRepository.findById(postId)
                                .orElseThrow(() -> new RuntimeException("Post not found"));

                // Kiểm tra nếu người dùng đã favorite bài viết này
                Optional<Favorite> existingFavorite = favoriteRepository.findByUserAndPost(user, post);

                if (existingFavorite.isPresent()) {
                        // Nếu đã favorite, thực hiện unfavorite
                        Favorite favorite = existingFavorite.get();

                        // Giảm giá trị favoriteCount (đảm bảo không giảm xuống dưới 0)
                        if (post.getFavoriteCount() > 0) {
                                post.setFavoriteCount(post.getFavoriteCount() - 1);
                        }

                        // Xóa liên kết giữa user, post và favorite
                        user.removeFavorite(favorite);
                        post.removeFavorite(favorite);

                        // Đánh dấu bài viết chưa được favorite
                        post.setFavorited(false);

                        // Xóa favorite khỏi cơ sở dữ liệu
                        favoriteRepository.delete(favorite);

                        return null; // Không trả về đối tượng Favorite vì đã thực hiện unfavorite
                } else {
                        // Nếu chưa favorite, thực hiện favorite
                        Favorite favorite = new Favorite();
                        user.addFavorite(favorite);
                        post.addFavorite(favorite);

                        // Tăng giá trị favoriteCount
                        post.setFavoriteCount(post.getFavoriteCount() + 1);

                        // Đánh dấu bài viết đã được favorite
                        post.setFavorited(true);

                        // gửi thông báo yêu thích
                        notificationService.notifyPostFavorite(user, post);

                        // Lưu đối tượng Favorite
                        return favoriteRepository.save(favorite);
                }
        }

        public boolean checkFavoriteStatus(Integer postId) {
                var context = SecurityContextHolder.getContext().getAuthentication();
                String name = context.getName();

                // Tìm User đang thực hiện hành động
                User user = userRepository.findByUsername(name)
                                .orElseThrow(() -> new RuntimeException("User not found"));
                Post post = postRepository.findById(postId)
                                .orElseThrow(() -> new RuntimeException("Post not found"));
                // Kiểm tra nếu có bản ghi trong bảng likes với userId và postId
                return favoriteRepository.existsByUserAndPost(user, post);
        }

}
