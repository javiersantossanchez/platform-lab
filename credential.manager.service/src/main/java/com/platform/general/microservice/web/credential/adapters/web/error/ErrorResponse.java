package com.platform.general.microservice.web.credential.adapters.web.error;

public class ErrorResponse {
    private String errorMessage;

    public ErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ErrorResponse() {
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
