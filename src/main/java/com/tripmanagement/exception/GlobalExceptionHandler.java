package com.tripmanagement.exception;

import com.tripmanagement.dto.ErrorResponseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, String> validationErrors = new HashMap<>();
        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
        allErrors.forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            validationErrors.put(fieldName, errorMessage);
        });

        ErrorResponseDto<Map<String,String>> errorResponseDto = new ErrorResponseDto<>();
        errorResponseDto.setApiPath(request.getDescription(false));
        errorResponseDto.setStatus(HttpStatus.valueOf(status.value()));
        errorResponseDto.setTimestamp(LocalDateTime.now());
        errorResponseDto.setMessage(validationErrors);
        return new ResponseEntity<>(errorResponseDto, HttpStatus.valueOf(status.value()));
    }

    @ExceptionHandler(TripException.class)
    public ResponseEntity<ErrorResponseDto<String>> handleMethodArgumentTypeMismatchException(TripException e, WebRequest request) {

        ErrorResponseDto<String> errorResponseDto = new ErrorResponseDto<>();
        errorResponseDto.setMessage(e.getMessage());
        errorResponseDto.setApiPath(request.getDescription(false));
        errorResponseDto.setStatus(e.getStatusCode());
        errorResponseDto.setTimestamp(LocalDateTime.now());
        return ResponseEntity.status(e.getStatusCode()).body(errorResponseDto);
    }


}
