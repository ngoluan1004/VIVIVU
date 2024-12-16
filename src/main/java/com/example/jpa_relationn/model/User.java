package com.example.jpa_relationn.model;

import lombok.*;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.jpa_relationn.enums.Role;
import com.example.jpa_relationn.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;
    private String username;
    private String email;
    private String password;
    private String fullName;
    private String imgUrl;
    private String backgroundImage;
    private String bio;
    private String birthDate;
    private String location;

    private Integer followingCount;
    private Integer followerCount;

    @Enumerated(EnumType.STRING)
    private Role role;
    @Enumerated(EnumType.STRING)
    private Status status;

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
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    public void addPost(Post post) {
        this.posts.add(post);
        post.setUser(this);
    }

    public void removePost(Post post) {
        post.setUser(null);
        this.posts.remove(post);
    }

    @JsonIgnore
    public List<Post> getListPost() {
        return this.posts;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    public void addComment(Comment comment) {
        this.comments.add(comment);
        comment.setUser(this);
    }

    public void removeComment(Comment comment) {
        comment.setUser(null);
        this.comments.remove(comment);
    }

    @JsonIgnore
    public List<Comment> getListComments() {
        return this.comments;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> reports = new ArrayList<>();

    public void addReport(Report report) {
        this.reports.add(report);
        report.setUser(this);
    }

    public void removeReport(Report report) {
        report.setUser(null);
        this.reports.remove(report);
    }

    @JsonIgnore
    public List<Report> getListReports() {
        return this.reports;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();

    public void addLike(Like like) {
        this.likes.add(like);
        like.setUser(this);
    }

    public void removeLike(Like like) {
        like.setUser(null);
        this.likes.remove(like);
    }

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RePost> reposts = new ArrayList<>();

    public void addRePost(RePost rePost) {
        this.reposts.add(rePost);
        rePost.setUser(this);
    }

    public void removeRePost(RePost rePost) {
        rePost.setUser(null);
        this.reposts.remove(rePost);
    }

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Favorite> favorites = new ArrayList<>();

    public void addFavorite(Favorite favorite) {
        this.favorites.add(favorite);
        favorite.setUser(this);
    }

    public void removeFavorite(Favorite favorite) {
        favorite.setUser(null);
        this.favorites.remove(favorite);
    }

    @JsonIgnore
    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> followings = new ArrayList<>();

    public void addFollowing(Follow following) {
        this.followings.add(following);
        following.setFollowing(this);
    }

    public void removeFollowing(Follow following) {
        following.setFollowing(null);
        this.followings.remove(following);
    }

    @JsonIgnore
    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> followers = new ArrayList<>();

    public void addFollower(Follow follower) {
        this.followers.add(follower);
        follower.setFollower(this);
    }

    public void removeFollower(Follow follower) {
        follower.setFollower(null);
        this.followers.remove(follower);
    }

    public List<Follow> getFollowers() {
        return this.followers;
    }

    public List<Follow> getFollowing() {
        return this.followings;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> sentMessages = new ArrayList<>();

    public void addSentMessage(Message senderMessage) {
        this.sentMessages.add(senderMessage);
        senderMessage.setSender(this);
    }

    public void removeMessage(Message senderMessage) {
        senderMessage.setSender(null);
        sentMessages.remove(senderMessage);
    }

    @JsonIgnore
    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> receivedMessages = new ArrayList<>();

    public void addReceiverMessage(Message receiveMessage) {
        this.receivedMessages.add(receiveMessage);
        receiveMessage.setReceiver(this);
    }

    public void removeReceiverMessage(Message receiveMessage) {
        receiveMessage.setReceiver(null);
        this.receivedMessages.remove(receiveMessage);
    }

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications = new ArrayList<>();

    public void addNotification(Notification notification) {
        this.notifications.add(notification);
        notification.setUser(this);
    }

    public void removeNotification(Notification notification) {
        notification.setUser(null);
        this.notifications.remove(notification);
    }

}
