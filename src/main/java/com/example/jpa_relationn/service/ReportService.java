package com.example.jpa_relationn.service;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.jpa_relationn.enums.ReportStatus;
import com.example.jpa_relationn.model.Comment;
import com.example.jpa_relationn.model.Post;
import com.example.jpa_relationn.model.Report;
import com.example.jpa_relationn.model.User;
import com.example.jpa_relationn.repository.CommentRepository;
import com.example.jpa_relationn.repository.PostRepository;
import com.example.jpa_relationn.repository.ReportRepository;
import com.example.jpa_relationn.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportService {
        PostRepository postRepository;
        UserRepository userRepository;
        ReportRepository reportRepository;
        CommentRepository commentRepository;

        public List<Report> getAllReportsSortedByIdDesc() {
                return reportRepository.findAllByOrderByReportIdDesc();
        }

        public Report reportPost(Integer postId, Report report) {
                var context = SecurityContextHolder.getContext().getAuthentication();
                String name = context.getName();
                User user = userRepository.findByUsername(name)
                                .orElseThrow(() -> new RuntimeException("User not found"));
                Post post = postRepository.findById(postId)
                                .orElseThrow(() -> new RuntimeException("Post not found"));

                Report reportDTO = new Report();
                reportDTO.setUser(user);
                reportDTO.setPost(post);
                reportDTO.setStatus(ReportStatus.OPEN);
                reportDTO.setReason(report.getReason());
                // reportDTO.setComment(report.getComment());

                return reportRepository.save(reportDTO);
        }

        public Report reportComment(Integer commentId, Report report) {
                var context = SecurityContextHolder.getContext().getAuthentication();
                String name = context.getName();
                User user = userRepository.findByUsername(name)
                                .orElseThrow(() -> new RuntimeException("User not found"));
                Comment cmt = commentRepository.findById(commentId)
                                .orElseThrow(() -> new RuntimeException("Post not found"));
                Report reportDTO = new Report();
                reportDTO.setUser(user);
                // reportDTO.setPost();
                reportDTO.setStatus(ReportStatus.OPEN);
                reportDTO.setReason(report.getReason());
                reportDTO.setComment(cmt);

                return reportRepository.save(reportDTO);
        }

        public void deleteReportById(Integer reportId) {
                // Kiểm tra nếu report tồn tại
                Report report = reportRepository.findById(reportId).orElseThrow();

                // Xóa report
                reportRepository.delete(report);
        }
}
