package com.example.jpa_relationn.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.jpa_relationn.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    List<User> findAllByUsername(String username);

    // tìm 10 bài đăng sắp xếp theo gần nhất
    List<User> findTop10ByOrderByUserIdDesc();
}