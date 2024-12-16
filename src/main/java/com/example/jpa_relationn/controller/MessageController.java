package com.example.jpa_relationn.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jpa_relationn.model.Message;
import com.example.jpa_relationn.service.MessageService;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/send/{receiverId}")
    public ResponseEntity<Message> sendMessage(@PathVariable("receiverId") Integer receiverId,
            @RequestBody Message content) {
        Message message = messageService.sendMessage(receiverId, content);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/conversation/{userId}")
    public ResponseEntity<List<Message>> getMessages(@PathVariable("userId") Integer userId) {
        List<Message> messages = messageService.getMessagesBetweenUsers(userId);
        return ResponseEntity.ok(messages);
    }
}