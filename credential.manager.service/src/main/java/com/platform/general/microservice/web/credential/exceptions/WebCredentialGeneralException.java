package com.platform.general.microservice.web.credential.exceptions;

public class WebCredentialGeneralException extends RuntimeException {


    public WebCredentialGeneralException(Exception exception) {
        super("An error was generated to search the credential",exception);
    }

    public WebCredentialGeneralException(String message, Throwable cause) {
        super(message, cause);
    }

    public WebCredentialGeneralException() {
    }

    public String getErrorMessage(){
        return this.getMessage();
    }

}
