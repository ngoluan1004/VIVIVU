package com.example.jpa_relationn.model;

import lombok.*;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.example.jpa_relationn.exception.AppException;

import com.example.jpa_relationn.enums.CommentStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer commentId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    // @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Lob
    private String content;

    @Enumerated(EnumType.STRING)
    private CommentStatus status;

    @Column(name = "created_at")
    @Temporal(TemporalType.DATE) // Chỉ lưu ngày (không có giờ)
    private Date createdAt;

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = new Date(); // Lưu ngày giờ hiện tại
        }
    }

    @JsonIgnore
    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> reports = new ArrayList<>();

    public void addReport(Report report) {
        this.reports.add(report);
        report.setComment(this);
    }

    public void removeReport(Report report) {
        report.setComment(null);
        this.reports.remove(report);
    }

    @JsonIgnore
    @OneToMany(mappedBy = "relatedComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications = new ArrayList<>();

    public void addNotification(Notification notification) {
        this.notifications.add(notification);
        notification.setRelatedComment(this);
    }

    public void removeNotification(Notification notification) {
        notification.setRelatedComment(null);
        this.notifications.remove(notification);
    }
}
