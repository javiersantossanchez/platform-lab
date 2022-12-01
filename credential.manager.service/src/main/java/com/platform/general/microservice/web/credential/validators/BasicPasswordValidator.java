package com.platform.general.microservice.web.credential.validators;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class BasicPasswordValidator implements PasswordValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(BasicPasswordValidator.class);

    /***
     *
     * A password will be valid when
     * Length > 10 characters
     * Has upper and lower case
     * digits are allows,but not mandatory
     * characters a-z | A-Z
     * not special character
     * not empty
     * not null
     */
    @Override
    public boolean isValid(String password){
        if(StringUtils.isBlank(password)){
            return false;
        }
        if(!StringUtils.isMixedCase(password)){
            return false;
        }
        if(StringUtils.containsWhitespace(password)){
            return false;
        }
        if(!StringUtils.isAlphanumeric(password)){
            return false;
        }
        return password.length() > 10;
    }
}
