package com.example.jpa_relationn.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jpa_relationn.model.Report;
import com.example.jpa_relationn.service.ReportService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reports")
public class ReportController {
    @Autowired
    private final ReportService reportService;

    @GetMapping("/list")
    public ResponseEntity<List<Report>> getAllReports() {
        List<Report> reports = reportService.getAllReportsSortedByIdDesc();
        return ResponseEntity.ok(reports);
    }

    @PostMapping("/add_post/{postId}")
    public ResponseEntity<Report> reportPost(@PathVariable("postId") Integer postId,
            @RequestBody Report reqReport) {
        Report report = reportService.reportPost(postId, reqReport);
        return ResponseEntity.status(HttpStatus.CREATED).body(report);
    }

    @PostMapping("/add_cmt/{commentId}")
    public ResponseEntity<Report> reportComment(@PathVariable("commentId") Integer commentId,
            @RequestBody Report reqReport) {
        Report report = reportService.reportComment(commentId, reqReport);
        return ResponseEntity.status(HttpStatus.CREATED).body(report);
    }

    @DeleteMapping("/{reportId}")
    public ResponseEntity<String> deleteReport(@PathVariable("reportId") Integer reportId) {
        reportService.deleteReportById(reportId);
        return ResponseEntity.ok("Đã xóa báo cáo");
    }

}
