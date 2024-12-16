package com.example.jpa_relationn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.jpa_relationn.model.QA;

@Repository
public interface QARepository extends JpaRepository<QA, Integer> {
    // Có thể thêm các phương thức tùy chỉnh nếu cần
    @Query("SELECT q FROM QA q ORDER BY q.id DESC")
    List<QA> findAllOrdered();
}