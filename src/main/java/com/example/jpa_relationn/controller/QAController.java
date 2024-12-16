package com.example.jpa_relationn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.jpa_relationn.model.QA;
import com.example.jpa_relationn.service.QAService;

import java.util.List;

@RestController
@RequestMapping("/qa")
public class QAController {

    @Autowired
    private QAService qaService;

    public QAController(QAService qaService) {
        this.qaService = qaService;
    }

    // API thêm QA mới
    @PostMapping("/send")
    public ResponseEntity<QA> addQA(@RequestBody QA content) {
        QA qa = qaService.saveQA(content);
        return ResponseEntity.ok(qa);
    }

    // API lấy tất cả QA
    @GetMapping
    public List<QA> getAllQA() {
        return qaService.getAllQA();
    }
}
