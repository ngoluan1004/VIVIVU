package com.example.jpa_relationn.model;

import lombok.*;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer messageId;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

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
