package com.platform.general.microservice.web.credential.adapters.web;

import com.platform.general.microservice.web.credential.exceptions.IllegalArgumentException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorHandler {
    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity notFoundError(IllegalArgumentException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(exception.getErrorMessage()));
    }
}
