package com.tripmanagement.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ResponseDto<T> {
    private HttpStatus statusCode;
    private String message;
    private T data;
}
