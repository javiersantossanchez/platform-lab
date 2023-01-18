package com.platform.general.microservice.web.credential.exceptions;

public class WebCredentialServiceNotAvailableException extends RuntimeException {


    public WebCredentialServiceNotAvailableException(Exception exception) {
        super("Search the credential functionality is not available",exception);
    }

    public WebCredentialServiceNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public WebCredentialServiceNotAvailableException() {
    }

    public String getErrorMessage(){
        return getMessage();
    }

}
