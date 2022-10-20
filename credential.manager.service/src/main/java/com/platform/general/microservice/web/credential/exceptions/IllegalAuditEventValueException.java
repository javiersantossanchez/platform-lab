package com.platform.general.microservice.web.credential.exceptions;

public class IllegalAuditEventValueException extends RuntimeException {

    public enum Argument {
        TYPE,
    }

    public enum Validation {
        NOT_EMPTY
    }

    private Argument argument;

    private Validation validationFailed;

    public IllegalAuditEventValueException(Argument argument, Validation validationFailed) {
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
