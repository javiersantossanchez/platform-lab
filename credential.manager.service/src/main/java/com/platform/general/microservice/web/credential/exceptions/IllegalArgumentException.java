package com.platform.general.microservice.web.credential.exceptions;

public class IllegalArgumentException extends RuntimeException {

    public enum Argument {
        USER_NAME,
        PASSWORD,
        WEB_SITE,
        ID,
        USER_ID
    }

    public enum Validation {
        NOT_EMPTY,
        DUPLICATED
    }

    private final Argument argument;

    private final Validation validationFailed;

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

    public String getErrorMessage(){
        return String.format("The value for %s should be %s", this.argument,this.validationFailed );
    }

}
