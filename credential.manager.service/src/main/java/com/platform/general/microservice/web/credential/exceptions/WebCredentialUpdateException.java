package com.platform.general.microservice.web.credential.exceptions;

public class WebCredentialUpdateException extends RuntimeException {


    public WebCredentialUpdateException(final Exception exception) {
        super(exception);
    }

    public WebCredentialUpdateException() {
    }

    public String getErrorMessage() {
        return "An error was generated during the credential update";
    }

}
