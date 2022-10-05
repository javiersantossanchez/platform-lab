package com.platform.general.microservice.web.credential;


import com.platform.general.microservice.web.credential.exceptions.InvalidPasswordException;
import com.platform.general.microservice.web.credential.ports.out.WebCredentialRepository;
import com.platform.general.microservice.web.credential.validators.PasswordValidator;
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
        if(!validator.isValid(password)) {
            throw new InvalidPasswordException();
        }

        return repository.save(password, userName, webSite);
    }
}
