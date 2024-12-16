package com.example.jpa_relationn.model;

import java.util.Date;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "qa")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QA {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer messageId;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @Lob
    private String content;

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
