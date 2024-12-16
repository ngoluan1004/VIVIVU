package com.example.jpa_relationn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.jpa_relationn.model.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {
    List<Report> findAllByOrderByReportIdDesc();
}
