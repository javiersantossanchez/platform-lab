package com.platform.general.microservice.web.credential.exceptions;

public class WebCredentialGeneralException extends RuntimeException {


    public WebCredentialGeneralException(Exception exception) {
        super(exception);
    }

    public WebCredentialGeneralException() {
    }

    public String getErrorMessage(){
        return "An error was generated to search the credential";
    }

}
