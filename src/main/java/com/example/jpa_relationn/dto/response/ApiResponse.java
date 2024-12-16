package com.example.jpa_relationn.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL) // khi chạy , field nào null sẽ không được hiển thị
// class này chứa tất cả các field ta mong muốn để chuẩn hóa các API của mình
// ApiResponse<T> đây là kiểu khai báo "generic"
// nghĩa là lồng đối tượng T thành 1 thuộc tính "result" trong "ApiResponse"
public class ApiResponse<T> {
    int code = 1000; // khai báo 1000 để biết nếu api nào trả về 1000 thì tức là thành công
    String message;
    T result;
}
