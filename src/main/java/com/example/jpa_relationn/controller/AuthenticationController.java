package com.example.jpa_relationn.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jpa_relationn.dto.response.ApiResponse;
import com.example.jpa_relationn.dto.request.AuthenticationRequest;
import com.example.jpa_relationn.dto.request.IntrospectRequest;
import com.example.jpa_relationn.dto.response.AuthenticationResponse;
import com.example.jpa_relationn.dto.response.IntrospectResponse;
import com.example.jpa_relationn.enums.Status;
import com.example.jpa_relationn.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import com.example.jpa_relationn.model.Favorite;
import com.example.jpa_relationn.model.User;
import com.example.jpa_relationn.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.text.ParseException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    AuthenticationService authenticationService;
    UserRepository userRepository;

    @PostMapping("/token")
    public ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        // Lấy thông tin người dùng từ cơ sở dữ liệu dựa trên username
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow();

        // Kiểm tra nếu người dùng không tồn tại hoặc trạng thái người dùng là BANNED
        if (user == null || user.getStatus() == Status.BANNED) {
            // Trả về lỗi nếu người dùng bị khóa
            return ApiResponse.<AuthenticationResponse>builder()
                    .message("User không tìm thấy hoặc đã bị khóa")
                    .build();
        }

        // Thực hiện xác thực nếu người dùng hợp lệ
        var result = authenticationService.authenticate(request);

        return ApiResponse.<AuthenticationResponse>builder()
                .result(result) // Lưu kết quả xác thực vào thuộc tính "result" của ApiResponse
                .build(); // Trả về đối tượng ApiResponse đã được xây dựng
    }

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {

        var result = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder()
                .result(result)
                .build();
    }

}
