package com.tripmanagement.exception;

import org.springframework.http.HttpStatus;

public class TripNotFoundException extends TripException{

    public TripNotFoundException(String message, HttpStatus statusCode) {
        super(message, statusCode);
    }
}
