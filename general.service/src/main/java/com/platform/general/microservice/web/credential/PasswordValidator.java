package com.platform.general.microservice.web.credential;

public interface PasswordValidator {
    boolean isValid(String password);
}
