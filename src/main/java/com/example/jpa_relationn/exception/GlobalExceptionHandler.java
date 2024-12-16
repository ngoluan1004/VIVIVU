package com.example.jpa_relationn.exception;

import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.jpa_relationn.dto.response.ApiResponse;

import lombok.extern.slf4j.Slf4j;

// trong tất cả các dự án thực tế đều có 1 file như thế này và tập trung tất cả các exception ở đây
@ControllerAdvice // báo cho spring biết đây là nơi tập trung tất cả các exception, khi có
                  // exception xảy ra thì class này sẽ chịu trách nghiệm
@Slf4j
public class GlobalExceptionHandler {

    // @ExceptionHandler(value = Exception.class)
    // public ResponseEntity<ApiResponse> handlingAppException(RuntimeException
    // exception) {
    // // ErrorCode errorCode = exception.getErrorCode();
    // log.error("Excoption: ", exception);
    // ApiResponse apiResponse = new ApiResponse<>();

    // apiResponse.setCode(ErrorCode.UNKNOW_EXCEPTION.getCode());
    // apiResponse.setMessage(ErrorCode.UNKNOW_EXCEPTION.getMessage());

    // // lỗi 400 tương ứng vs badrequest
    // return ResponseEntity.badRequest().body(apiResponse);
    // }

    // khi có 1 exception theo kiểu runtime exception xảy ra thì nó đều sẽ tập trung
    // về đây để xử lý
    // @ExceptionHandler(value = RuntimeException.class) // bắt lỗi thì dùng cái
    // này(param là class mà chúng ta muốn bắt )
    // // khi khai báo param RuntimeException vào method thì spring nó sẽ inject nó
    // vào
    // // cái Exception param này
    // public ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException
    // exception) {
    // ApiResponse apiResponse = new ApiResponse<>();

    // apiResponse.setCode(1001);
    // apiResponse.setMessage(exception.getMessage());

    // // lỗi 400 tương ứng vs badrequest
    // return ResponseEntity.badRequest().body(apiResponse);
    // }

    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<ApiResponse> handlingAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        // lỗi 400 tương ứng vs badrequest
        return ResponseEntity
                .badRequest()
                .body(apiResponse);
    }

    // @ExceptionHandler(value = AccessDeniedException.class)
    // public ResponseEntity<ApiResponse>
    // handlingAccessDeniedException(AccessDeniedException exception) {
    // ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

    // return ResponseEntity.status(errorCode.getStatusCode()).body(
    // ApiResponse.builder()
    // .code(errorCode.getCode())
    // .message(errorCode.getMessage())
    // .build());
    // }

    // @ExceptionHandler(value = MethodArgumentNotValidException.class)
    // public ResponseEntity<ApiResponse>
    // handlingValidation(MethodArgumentNotValidException exception) {
    // String enumkey = exception.getFieldError().getDefaultMessage();
    // ErrorCode errorCode = ErrorCode.valueOf(enumkey);

    // ApiResponse apiResponse = new ApiResponse<>();

    // apiResponse.setCode(errorCode.getCode());
    // apiResponse.setMessage(errorCode.getMessage());

    // return ResponseEntity.badRequest().body(apiResponse);
    // }

    // hàm này để xử lý nếu có lỗi sai trong validation trong đối tượng dto
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handlingValidation(MethodArgumentNotValidException exception) {
        String enumkey = exception.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.INVALID_KEY;

        try {
            errorCode = ErrorCode.valueOf(enumkey);
        } catch (IllegalArgumentException e) {
        }

        ApiResponse apiResponse = new ApiResponse<>();

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }

    // @ExceptionHandler(value = MethodArgumentNotValidException.class)
    // public ResponseEntity<String>
    // handlingValidation(MethodArgumentNotValidException exception) {
    // return
    // ResponseEntity.badRequest().body(exception.getFieldError().getDefaultMessage());
    // }
}
