package com.example.jpa_relationn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.jpa_relationn.model.Notification;
import com.example.jpa_relationn.model.User;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByUserAndIsReadFalse(User user);

    List<Notification> findAllByUserOrderByNotificationIdDesc(User user);
}
