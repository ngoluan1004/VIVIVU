package com.example.jpa_relationn.model;

import lombok.*;
import jakarta.persistence.*;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

@Entity
@Table(name = "follows")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer followId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "following_id", nullable = false)
    private User following;

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
