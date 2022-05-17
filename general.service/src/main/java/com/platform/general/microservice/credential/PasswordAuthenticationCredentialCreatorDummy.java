package com.platform.general.microservice.credential;

public class PasswordAuthenticationCredentialCreatorDummy implements  PasswordAuthenticationCredentialCreator {
    public PasswordAuthenticationCredential create(String password,String userName){
        return new PasswordAuthenticationCredential(password,userName);
    }

}
