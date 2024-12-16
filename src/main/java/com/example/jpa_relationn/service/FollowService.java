package com.example.jpa_relationn.service;

import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.repository.query.Param;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.jpa_relationn.exception.AppException;
import com.example.jpa_relationn.exception.ErrorCode;
import com.example.jpa_relationn.model.Follow;
import com.example.jpa_relationn.model.Like;
import com.example.jpa_relationn.model.Post;
import com.example.jpa_relationn.model.User;
import com.example.jpa_relationn.repository.FollowRepository;
import com.example.jpa_relationn.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FollowService {
        UserRepository userRepository;
        FollowRepository followRepository;
        NotificationService notificationService;

        // Lấy danh sách các user mà user A đang theo dõi
        public List<User> getUsersFollowing(Integer followerId) {
                List<User> users = followRepository.findUsersFollowingByFollowerId(followerId);
                List<User> lusers = users.stream().map(user -> {
                        User userDTO = new User();
                        userDTO.setUserId(user.getUserId());
                        userDTO.setUsername(user.getUsername());
                        userDTO.setEmail(user.getEmail());
                        userDTO.setPassword(user.getPassword());
                        userDTO.setFullName(user.getFullName());
                        userDTO.setImgUrl("/images/" + Paths.get(user.getImgUrl()).getFileName().toString());
                        userDTO.setBackgroundImage(
                                        "/images/" + Paths.get(user.getBackgroundImage()).getFileName().toString());
                        userDTO.setBio(user.getBio());
                        userDTO.setBirthDate(user.getBirthDate());
                        userDTO.setLocation(user.getLocation());
                        userDTO.setRole(user.getRole());
                        userDTO.setStatus(user.getStatus());
                        userDTO.setCreatedAt(user.getCreatedAt());
                        userDTO.setFollowingCount(user.getFollowings().size());
                        userDTO.setFollowerCount(user.getFollowers().size());
                        return userDTO;
                }).collect(Collectors.toList());
                return lusers;
        }

        // Lấy danh sách các user đang follow user A
        public List<User> getUsersFollower(Integer followingId) {
                List<User> users = followRepository.findUsersFollowerByFollowingId(followingId);
                List<User> lusers = users.stream().map(user -> {
                        User userDTO = new User();
                        userDTO.setUserId(user.getUserId());
                        userDTO.setUsername(user.getUsername());
                        userDTO.setEmail(user.getEmail());
                        userDTO.setPassword(user.getPassword());
                        userDTO.setFullName(user.getFullName());
                        userDTO.setImgUrl("/images/" + Paths.get(user.getImgUrl()).getFileName().toString());
                        userDTO.setBackgroundImage(
                                        "/images/" + Paths.get(user.getBackgroundImage()).getFileName().toString());
                        userDTO.setBio(user.getBio());
                        userDTO.setBirthDate(user.getBirthDate());
                        userDTO.setLocation(user.getLocation());
                        userDTO.setRole(user.getRole());
                        userDTO.setStatus(user.getStatus());
                        userDTO.setCreatedAt(user.getCreatedAt());
                        userDTO.setFollowingCount(user.getFollowings().size());
                        userDTO.setFollowerCount(user.getFollowers().size());
                        return userDTO;
                }).collect(Collectors.toList());
                return lusers;
        }

        // theo dõi user
        public Follow followUser(Integer userIdToFollow) {
                var context = SecurityContextHolder.getContext().getAuthentication();
                String username = context.getName();

                // Tìm User đang thực hiện hành động (người theo dõi)
                User currentUser = userRepository.findByUsername(username)
                                .orElseThrow(() -> new RuntimeException("Current user not found"));

                // Tìm User cần Follow
                User userToFollow = userRepository.findById(userIdToFollow)
                                .orElseThrow(() -> new RuntimeException("User to follow not found"));

                // Không cho phép người dùng tự Follow chính mình
                if (currentUser.equals(userToFollow)) {
                        throw new IllegalArgumentException("Users cannot follow themselves");
                }

                // Kiểm tra nếu người dùng đã Follow user này trước đó
                Optional<Follow> existingFollow = followRepository.findByFollowerAndFollowing(
                                userToFollow, currentUser);

                if (existingFollow.isPresent()) {
                        // Nếu đã Follow, thực hiện Unfollow
                        Follow follow = existingFollow.get();

                        // Xóa liên kết giữa currentUser và userToFollow
                        currentUser.removeFollowing(follow);
                        userToFollow.removeFollower(follow);

                        // Xóa follow khỏi cơ sở dữ liệu
                        followRepository.delete(follow);

                        return null; // Không trả về đối tượng Follow vì là Unfollow
                }

                // Nếu chưa Follow, thực hiện Follow
                Follow follow = new Follow();
                currentUser.addFollowing(follow);
                userToFollow.addFollower(follow);

                // Tăng số lượng người theo dõi của user được Follow
                // userToFollow.setFollowersCount(userToFollow.getFollowersCount() + 1);

                // gửi thông báo có user theo dõi
                notificationService.notifyFollow(currentUser, userToFollow);

                // Lưu đối tượng Follow
                return followRepository.save(follow);
        }

        // kiểm tra trạng thái đã theo dõi hay chưa
        public boolean checkFollowStatus(Integer userId) {
                var context = SecurityContextHolder.getContext().getAuthentication();
                String name = context.getName();

                // Tìm User đang thực hiện hành động
                User user1 = userRepository.findByUsername(name)
                                .orElseThrow(() -> new RuntimeException("User not found"));
                User user2 = userRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("User not found"));
                // Kiểm tra nếu có bản ghi trong bảng likes với userId và postId
                return followRepository.existsByFollowerAndFollowing(user2, user1);
        }
}
