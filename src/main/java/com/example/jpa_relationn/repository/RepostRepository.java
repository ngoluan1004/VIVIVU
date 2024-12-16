package com.example.jpa_relationn.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.jpa_relationn.model.Like;
import com.example.jpa_relationn.model.Post;
import com.example.jpa_relationn.model.RePost;
import com.example.jpa_relationn.model.User;

public interface RepostRepository extends JpaRepository<RePost, Integer> {
    Optional<RePost> findByUserAndPost(User user, Post post);

    boolean existsByUserAndPost(User user, Post post);
}
