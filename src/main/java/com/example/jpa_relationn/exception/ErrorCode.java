package com.example.jpa_relationn.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNKNOW_EXCEPTION(9999, "User unkown", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXISTED(1001, "User existed", HttpStatus.BAD_REQUEST),
    INVALID_KEY(1002, "invalid message key", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be at least 3 character", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Password must be at least 8 character", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Sai tên đăng nhập hoặc mật khẩu", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have a permission", HttpStatus.FORBIDDEN);

    private int code;
    private String message;
    private HttpStatusCode statusCode;
}
