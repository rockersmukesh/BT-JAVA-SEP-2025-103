package com.tripmanagement.exception;

import org.springframework.http.HttpStatus;

public class TripAlreadyExistsException extends TripException{

    public TripAlreadyExistsException(String message, HttpStatus statusCode) {
        super(message, statusCode);
    }
}
