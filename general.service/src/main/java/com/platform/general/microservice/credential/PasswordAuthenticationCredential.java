package com.platform.general.microservice.credential;

import com.platform.general.microservice.web.credential.validators.BasicPasswordValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PasswordAuthenticationCredential {

    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordAuthenticationCredential.class);

    private BasicPasswordValidator passwordValidator;

    private String password;

    private String userName;

    public static PasswordAuthenticationCredentialCreatorDummy creator(){
       return new PasswordAuthenticationCredentialCreatorDummy();
    }

    PasswordAuthenticationCredential(){

    }

    public PasswordAuthenticationCredential(String password, String userName) {
        this.passwordValidator = new BasicPasswordValidator();
        if(!this.passwordValidator.isValid(password)){
            throw new IllegalArgumentException(String.format("The [PASSWORD] param is invalid",password));
        }
        this.password = password;
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public String toString() {
        return "PasswordAuthenticationCredential{" +
                "password='" + password + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
