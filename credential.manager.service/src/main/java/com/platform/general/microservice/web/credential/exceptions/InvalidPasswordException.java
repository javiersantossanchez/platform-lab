package com.platform.general.microservice.web.credential.exceptions;

public class InvalidPasswordException extends  RuntimeException{
    public final static String errorMessage ="Invalid password";

    public String getErrorMessage(){
        return  errorMessage;
    }
}
