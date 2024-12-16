package com.example.jpa_relationn.model;

import lombok.*;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.jpa_relationn.enums.PostStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer postId;
    private String title;
    private String content;
    @Enumerated(EnumType.STRING)
    private PostStatus status;
    private String address;
    private String category;
    private String image;

    private Integer likesCount;
    private Integer repostCount;
    private Integer favoriteCount;
    private Integer commentCount;
    private boolean liked;
    private boolean reposted;
    private boolean favorited;

    @Column(name = "created_at")
    @Temporal(TemporalType.DATE) // Chỉ lưu ngày (không có giờ)
    private Date createdAt;

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = new Date(); // Lưu ngày giờ hiện tại
        }
    }

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    public void addComment(Comment comment) {
        this.comments.add(comment);
        comment.setPost(this);
    }

    public void removeComment(Comment comment) {
        comment.setPost(null);
        this.comments.remove(comment);
    }

    public List<Comment> getAllComments() {
        return this.comments;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> reports = new ArrayList<>();

    public void addReport(Report report) {
        this.reports.add(report);
        report.setPost(this);
    }

    public void removeReport(Report report) {
        report.setPost(null);
        this.reports.remove(report);
    }

    @JsonIgnore
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();

    public void addLike(Like like) {
        this.likes.add(like);
        like.setPost(this);
    }

    public void removeLike(Like like) {
        like.setPost(null);
        this.likes.remove(like);
    }

    @JsonIgnore
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RePost> reposts = new ArrayList<>();

    public void addRePost(RePost rePost) {
        this.reposts.add(rePost);
        rePost.setPost(this);
    }

    public void removeRePost(RePost rePost) {
        rePost.setPost(null);
        this.reposts.remove(rePost);
    }

    @JsonIgnore
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Favorite> favorites = new ArrayList<>();

    public void addFavorite(Favorite favorite) {
        this.favorites.add(favorite);
        favorite.setPost(this);
    }

    public void removeFavorite(Favorite favorite) {
        favorite.setPost(null);
        this.favorites.remove(favorite);
    }

    // @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval =
    // true)
    // private List<Image> images = new ArrayList<>();

    // public void addImage(Image image) {
    // this.images.add(image);
    // image.setPost(this);
    // }

    // public void removeImage(Image image) {
    // image.setPost(null);
    // this.images.remove(image);
    // }

    @JsonIgnore
    @OneToMany(mappedBy = "relatedPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications = new ArrayList<>();

    public void addNotification(Notification notification) {
        this.notifications.add(notification);
        notification.setRelatedPost(this);
    }

    public void removeNotification(Notification notification) {
        notification.setRelatedPost(null);
        this.notifications.remove(notification);
    }
}
