package com.example.jpa_relationn.dto.request;

import lombok.Data;

@Data
public class MessageDto {
    private String sender; // Username người gửi
    private String receiver; // Username người nhận
    private String content; // Nội dung tin nhắn
    private String timestamp; // Thời gian gửi
}
