package com.tripmanagement.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class TripException extends RuntimeException {
    private final HttpStatus statusCode;

    public TripException(String message,HttpStatus statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
