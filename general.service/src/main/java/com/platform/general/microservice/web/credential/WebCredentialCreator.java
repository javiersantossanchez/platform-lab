package com.platform.general.microservice.web.credential;


import com.platform.general.microservice.exceptions.IllegalArgumentException;
import com.platform.general.microservice.web.credential.exceptions.InvalidPasswordException;
import com.platform.general.microservice.web.credential.ports.out.WebCredentialRepository;
import com.platform.general.microservice.web.credential.validators.PasswordValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WebCredentialCreator {

    private WebCredentialRepository repository;

    private PasswordValidator validator;

    @Autowired
    public WebCredentialCreator(WebCredentialRepository repository, PasswordValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    public WebCredential create(String password, String userName, String webSite) {
        if(StringUtils.isBlank(password)){
            throw new IllegalArgumentException(IllegalArgumentException.Argument.PASSWORD, IllegalArgumentException.Validation.NOT_EMPTY);
        }
        if(StringUtils.isBlank(userName)){
            throw new IllegalArgumentException(IllegalArgumentException.Argument.USER_NAME, IllegalArgumentException.Validation.NOT_EMPTY);
        }
        if(StringUtils.isBlank(webSite)){
            throw new IllegalArgumentException(IllegalArgumentException.Argument.WEB_SITE, IllegalArgumentException.Validation.NOT_EMPTY);
        }
        if(!validator.isValid(password)) {
            throw new InvalidPasswordException();
        }

        return repository.save(password, userName, webSite);
    }
}
