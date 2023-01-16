package com.platform.general.microservice.web.credential.adapters.web.error;

import com.platform.general.microservice.web.credential.exceptions.*;
import com.platform.general.microservice.web.credential.exceptions.IllegalArgumentException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;


@ControllerAdvice
public class ErrorHandler {
    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity notFoundError(IllegalArgumentException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(exception.getErrorMessage()));
    }

    @ExceptionHandler({InvalidArgumentException.class})
    public ResponseEntity InvalidArgumentExceptionHandler(InvalidArgumentException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(exception.getErrorMessage()));
    }

    @ExceptionHandler({InvalidUserInformationException.class})
    public ResponseEntity notFoundError(InvalidUserInformationException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(exception.getErrorMessage()));
    }

    @ExceptionHandler({WebCredentialNotFoundException.class})
    public ResponseEntity notFoundError(WebCredentialNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(exception.getErrorMessage()));
    }

    @ExceptionHandler({WebCredentialSearchException.class})
    public ResponseEntity notFoundError(WebCredentialSearchException exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(exception.getErrorMessage()));
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity asdds(MethodArgumentTypeMismatchException exception) {
        String error = "Invalid value for "+exception.getName();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(error));
    }

    @ExceptionHandler({WebCredentialSearchNotAvailableException.class})
    public ResponseEntity searchNotAvailable(WebCredentialSearchNotAvailableException exception) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new ErrorResponse(exception.getErrorMessage()));
    }

    @ExceptionHandler({InvalidPasswordException.class})
    public ResponseEntity searchNotAvailable(InvalidPasswordException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(exception.getErrorMessage()));
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    public ResponseEntity searchNotAvailable(MissingServletRequestParameterException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(String.format("The parameter %s of type $s is required",exception.getParameterName(),exception.getParameterType())));
    }
}
