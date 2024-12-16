package com.example.jpa_relationn.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.jpa_relationn.model.Post;
import com.example.jpa_relationn.model.User;

import jakarta.transaction.Transactional;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    // tìm tất cả bài đăng
    List<Post> findAllByOrderByCreatedAtDesc();

    // tìm 10 bài đăng sắp xếp theo gần nhất
    List<Post> findTop10ByOrderByPostIdDesc();

    List<Post> findByLikesContainingOrderByCreatedAtDesc(User user);

    // tìm kiếm theo tên bài đăng
    @Query("SELECT p FROM Post p WHERE (:title IS NULL OR :title = '' OR p.title LIKE %:title%)")
    List<Post> searchByTitle(@Param("title") String title);

    // tìm kiếm theo thể loại
    @Query("SELECT p FROM Post p WHERE (:category IS NULL OR :category = '' OR p.category LIKE %:category%)")
    List<Post> searchByCategory(@Param("category") String category);

    // lấy ra các bài đăng user đã like
    @Query("SELECT p FROM Post p JOIN p.likes l WHERE l.user.userId = :userId ORDER BY p.postId DESC")
    List<Post> findAllPostsLikedByUser(@Param("userId") Integer userId);

    // // lấy ra các bài đăng user đã repost
    @Query("SELECT p FROM Post p JOIN p.reposts r WHERE r.user.userId = :userId ORDER BY p.postId DESC")
    List<Post> findAllPostsRepostedByUser(@Param("userId") Integer userId);

    // lấy ra các bài đăng user đã yêu thích
    @Query("SELECT p FROM Post p JOIN p.favorites f WHERE f.user.userId = :userId ORDER BY p.postId DESC")
    List<Post> findAllPostsFavoritedByUser(@Param("userId") Integer userId);

}
