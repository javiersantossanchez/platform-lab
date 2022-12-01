package com.platform.general.microservice.web.credential.exceptions;

public class WebCredentialNotFoundException extends RuntimeException {


    public WebCredentialNotFoundException(Exception exception) {
        super(exception);
    }

    public WebCredentialNotFoundException() {
    }

    public String getErrorMessage(){
        return "The credential does not exist";
    }

}
