package com.example.jpa_relationn.model;

import lombok.*;
import jakarta.persistence.*;
import java.util.Date;

import com.example.jpa_relationn.enums.NotidicationType;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer notificationId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Lob
    private String content;

    private Boolean isRead;

    @Enumerated(EnumType.STRING)
    private NotidicationType type;

    @ManyToOne
    @JoinColumn(name = "related_post_id")
    private Post relatedPost;

    @ManyToOne
    @JoinColumn(name = "related_comment_id")
    private Comment relatedComment;

    @Column(name = "created_at")
    @Temporal(TemporalType.DATE) // Chỉ lưu ngày (không có giờ)
    private Date createdAt;

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = new Date(); // Lưu ngày giờ hiện tại
        }
    }
}
