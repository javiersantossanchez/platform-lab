package com.platform.general.microservice.web.credential.exceptions;

public class WebCredentialSearchNotAvailableException extends RuntimeException {


    public WebCredentialSearchNotAvailableException(Exception exception) {
        super(exception);
    }

    public WebCredentialSearchNotAvailableException() {
    }

    public String getErrorMessage(){
        return "Search the credential functionality is not available";
    }

}
