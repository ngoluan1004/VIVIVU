package com.example.jpa_relationn.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.jpa_relationn.model.Follow;
import com.example.jpa_relationn.model.User;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Integer> {
    Optional<Follow> findByFollowerAndFollowing(User follower, User following);

    // Kiểm tra trạng thái follow
    boolean existsByFollowerAndFollowing(User follower, User following);

    // Lấy danh sách tất cả người theo dõi của 1 user
    List<Follow> findByFollowing(User user);

    // Hoặc nếu bạn chỉ cần danh sách User thay vì Follow
    @Query("SELECT f.follower FROM Follow f WHERE f.following = :user")
    List<User> findFollowersByUser(@Param("user") User user);

    // Tìm danh sách các user mà user A (follower) đang theo dõi
    @Query("SELECT f.following FROM Follow f WHERE f.follower.userId = :followerId")
    List<User> findUsersFollowingByFollowerId(@Param("followerId") Integer followerId);

    // Tìm danh sách các user đang follow user A (following)
    @Query("SELECT f.follower FROM Follow f WHERE f.following.userId = :followingId")
    List<User> findUsersFollowerByFollowingId(@Param("followingId") Integer followingId);
}
