package com.example.jpa_relationn.repository;

import java.util.Optional;

import com.example.jpa_relationn.model.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.jpa_relationn.model.Like;
import com.example.jpa_relationn.model.Post;
import com.example.jpa_relationn.model.User;

@Repository
public interface LikeRepository extends JpaRepository<Like, Integer> {
    Optional<Like> findByUserAndPost(User user, Post post);

    boolean existsByUserAndPost(User user, Post post);

    // boolean existsByUserIdAndPostId(Integer userId, Integer postId);

    // @Query("SELECT l FROM Like l WHERE l.user.id=:userId AND l.post.id=:postId")
    // public Like isLikeExist(@Param("userId") Integer userId, @Param("postId")
    // Integer postId);

    // @Query("SELECT l FROM Like l WHERE l.post.id=:postId")
    // public List<Like> findByPostId(@Param("postId") Integer postId);
}
