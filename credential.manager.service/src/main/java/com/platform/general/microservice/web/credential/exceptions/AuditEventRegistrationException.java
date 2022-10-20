package com.platform.general.microservice.web.credential.exceptions;

public class AuditEventRegistrationException extends RuntimeException {


    public AuditEventRegistrationException(Exception exception) {
        super(exception);
    }

    public String getErrorMessage(){
        return String.format("An error was generated during the Audit Event information");
    }

}
