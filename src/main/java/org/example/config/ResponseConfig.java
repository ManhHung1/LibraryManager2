package org.example.config;

import org.example.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseConfig<T> {

    public static final String SUCCESS_CODE = "00";
    public static final String ERROR_CODE = "01";

    // Success response with data
    public static <T> ResponseEntity<ResponseDto<T>> success(T body) {
        ResponseDto responseDto = ResponseDto.builder().data(body).code(SUCCESS_CODE).build();
        return new ResponseEntity(responseDto, HttpStatus.OK);
    }

    // Success response for delete operations with a success flag
    public static <T> ResponseEntity<ResponseDto<T>> successDelete(T body, Boolean success) {
        if (success) {
            ResponseDto responseDto = ResponseDto.builder().data(body).code(SUCCESS_CODE).build();
            return new ResponseEntity(responseDto, HttpStatus.OK);
        } else {
            ResponseDto responseDto = ResponseDto.builder().data(body).code(ERROR_CODE).build();
            return new ResponseEntity(responseDto, HttpStatus.OK);
        }
    }

    // Error response with custom status and message
    public static ResponseEntity error(HttpStatus httpStatus, String errorCode, String message) {
        ResponseDto responseData = ResponseDto.builder().code(errorCode).message(message).build();
        return new ResponseEntity(responseData, httpStatus);
    }

    // Error response with custom status, content, and code
    public static <T> ResponseEntity<T> error(HttpStatus httpStatus, T content, String code) {
        ResponseDto responseData = ResponseDto.builder().code(code).data(content).build();
        return new ResponseEntity(responseData, httpStatus);
    }

}