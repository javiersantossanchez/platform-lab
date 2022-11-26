package com.platform.general.microservice.web.credential.adapters.web.error;

import com.platform.general.microservice.web.credential.exceptions.IllegalArgumentException;
import com.platform.general.microservice.web.credential.exceptions.InvalidUserInformationException;
import com.platform.general.microservice.web.credential.exceptions.WebCredentialNotFoundException;
import com.platform.general.microservice.web.credential.exceptions.WebCredentialSearchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;


@ControllerAdvice
public class ErrorHandler {
    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity notFoundError(IllegalArgumentException exception) {
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
}
