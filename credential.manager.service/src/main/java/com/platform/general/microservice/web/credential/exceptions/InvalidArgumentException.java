package com.platform.general.microservice.web.credential.exceptions;

public class InvalidArgumentException extends RuntimeException {

    public enum Error {
        PAGE_NUMBER_ON_PAGING_SHOULD_BE_BIGGER_THAN_ZERO("page number should be bigger than zero"),
        PAGE_SIZE_ON_PAGING_SHOULD_BE_BIGGER_THAN_ZERO("page size should be bigger than one"),

        USER_ID_SHOULD_BE_NOT_NULL("User id should be not null"),
        CREDENTIAL_ID_SHOULD_BE_NOT_NULL("Credential id should be not null"),
        PASSWORD_SIZE_BIGGER_THAN_VALUE_ALLOWS("Password is bigger than required")
        ;

        private String errorMessage;

        private Error (String errorMessage){
            this.errorMessage = errorMessage;
        }

        public String getMessage(){
            return this.errorMessage;
        }
    }

    private Error error;

    public InvalidArgumentException(Error error){
       this.error = error;
    }


    public String getErrorMessage(){
        return error.errorMessage;
    }

    public Error getError() {
        return error;
    }
}
