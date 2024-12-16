package com.example.jpa_relationn.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jpa_relationn.model.Notification;
import com.example.jpa_relationn.service.NotificationService;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<Notification>> getUserNotifications() {
        List<Notification> notifications = notificationService.getAllNotification();
        return ResponseEntity.ok(notifications);
    }
}
