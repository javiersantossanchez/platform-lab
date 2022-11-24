package com.platform.general.microservice.web.credential.exceptions;

public class InvalidUserInformationException extends RuntimeException{

    public String getErrorMessage(){
        return String.format("The user information is invalid");
    }

}
