package com.platform.general.microservice.web.credential.exceptions;

public class WebCredentialDeleteException extends RuntimeException {


    public WebCredentialDeleteException(Exception exception) {
        super(exception);
    }

    public WebCredentialDeleteException() {
    }

    public String getErrorMessage(){
        return "An error to delete the credential";
    }

}
