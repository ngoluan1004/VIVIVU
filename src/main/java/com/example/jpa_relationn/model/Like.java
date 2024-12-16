package com.example.jpa_relationn.model;

import lombok.*;
import jakarta.persistence.*;
import java.util.Date;
import com.example.jpa_relationn.exception.AppException;

@Entity
@Table(name = "likes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer likeId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

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
