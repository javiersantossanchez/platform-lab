package com.platform.general.microservice.web.credential.exceptions;

public class InvalidUserInformationException extends RuntimeException{

    public String getErrorMessage(){
        return "The user information is invalid";
    }

}
