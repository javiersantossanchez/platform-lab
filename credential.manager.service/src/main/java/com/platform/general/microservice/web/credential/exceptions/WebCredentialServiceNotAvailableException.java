package com.platform.general.microservice.web.credential.exceptions;

public class WebCredentialServiceNotAvailableException extends RuntimeException {


    public WebCredentialServiceNotAvailableException(Exception exception) {
        super(exception);
    }

    public WebCredentialServiceNotAvailableException() {
    }

    public String getErrorMessage(){
        return "Search the credential functionality is not available";
    }

}
