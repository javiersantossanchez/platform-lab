package com.platform.general.microservice.exceptions;

public class IllegalArgumentException extends RuntimeException {

    public enum Argument {
        USER_NAME,
        PASSWORD,
        WEB_SITE
    }

    public enum Validation {
        NOT_EMPTY
    }

    private Argument argument;

    private Validation validationFailed;

    public IllegalArgumentException(Argument argument, Validation validationFailed) {
        this.argument = argument;
        this.validationFailed = validationFailed;
    }

    public Argument getArgument() {
        return argument;
    }

    public Validation getValidationFailed() {
        return validationFailed;
    }
}
