package com.platform.general.microservice.web.credential.exceptions;

public class EmptyPagingParameterException extends RuntimeException {

    public String getErrorMessage() {
        return "The user id can not be null";
    }

}
