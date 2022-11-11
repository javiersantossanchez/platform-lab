package com.platform.general.microservice.web.credential.exceptions;

public class WebCredentialRegistrationException extends RuntimeException {


    public WebCredentialRegistrationException(Exception exception) {
        super(exception);
    }

    public WebCredentialRegistrationException() {
    }

    public String getErrorMessage(){
        return String.format("An error was generated during the credential creation");
    }

}
