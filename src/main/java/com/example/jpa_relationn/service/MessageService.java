package com.example.jpa_relationn.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.jpa_relationn.exception.AppException;
import com.example.jpa_relationn.exception.ErrorCode;
import com.example.jpa_relationn.model.Message;
import com.example.jpa_relationn.model.User;
import com.example.jpa_relationn.repository.MessageRepository;
import com.example.jpa_relationn.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import lombok.AccessLevel;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageService {
        MessageRepository messageRepository;
        UserRepository userRepository;

        public Message sendMessage(Integer receiverId, Message content) {
                // Lấy người gửi từ SecurityContextHolder
                var context = SecurityContextHolder.getContext().getAuthentication();
                String senderUsername = context.getName();

                // Tìm User người gửi
                User sender = userRepository.findByUsername(senderUsername).orElseThrow(
                                () -> new AppException(ErrorCode.USER_NOT_EXISTED));

                // Tìm User người nhận
                User receiver = userRepository.findById(receiverId).orElseThrow(
                                () -> new AppException(ErrorCode.USER_NOT_EXISTED));

                // Tạo và lưu tin nhắn
                Message message = new Message();
                message.setSender(sender);
                message.setReceiver(receiver);
                message.setContent(content.getContent());
                // message.setCreatedAt(new Date()); // Được xử lý thêm trong @PrePersist

                return messageRepository.save(message);
        }

        public List<Message> getMessagesBetweenUsers(Integer userId) {
                // Lấy người dùng hiện tại từ SecurityContextHolder
                var context = SecurityContextHolder.getContext().getAuthentication();
                String currentUsername = context.getName();

                User currentUser = userRepository.findByUsername(currentUsername).orElseThrow(
                                () -> new AppException(ErrorCode.USER_NOT_EXISTED));

                User otherUser = userRepository.findById(userId).orElseThrow(
                                () -> new AppException(ErrorCode.USER_NOT_EXISTED));

                // Lấy tất cả tin nhắn giữa hai người dùng
                return messageRepository.findMessagesBetweenUsers(currentUser.getUserId(), otherUser.getUserId());
        }
}
