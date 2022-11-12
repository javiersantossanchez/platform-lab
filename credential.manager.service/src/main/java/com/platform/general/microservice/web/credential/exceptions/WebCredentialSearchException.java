package com.platform.general.microservice.web.credential.exceptions;

public class WebCredentialSearchException extends RuntimeException {


    public WebCredentialSearchException(Exception exception) {
        super(exception);
    }

    public WebCredentialSearchException() {
    }

    public String getErrorMessage(){
        return String.format("An error was generated to search the credential");
    }

}
