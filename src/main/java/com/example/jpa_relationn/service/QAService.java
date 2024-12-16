package com.example.jpa_relationn.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.jpa_relationn.exception.AppException;
import com.example.jpa_relationn.exception.ErrorCode;
import com.example.jpa_relationn.model.Message;
import com.example.jpa_relationn.model.QA;
import com.example.jpa_relationn.model.User;
import com.example.jpa_relationn.repository.QARepository;
import com.example.jpa_relationn.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import lombok.AccessLevel;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QAService {

    QARepository qaRepository;
    UserRepository userRepository;

    public QA saveQA(QA qa) {
        // Lấy người gửi từ SecurityContextHolder
        var context = SecurityContextHolder.getContext().getAuthentication();
        String senderUsername = context.getName();

        // Tìm User người gửi
        User sender = userRepository.findByUsername(senderUsername).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Gán sender vào qa được truyền vào
        qa.setSender(sender);

        // Lưu thông tin QA vào DB
        return qaRepository.save(qa);
    }

    public List<QA> getAllQA() {
        return qaRepository.findAllOrdered();
    }

}
