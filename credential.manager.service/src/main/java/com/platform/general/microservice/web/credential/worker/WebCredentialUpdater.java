package com.platform.general.microservice.web.credential.worker;


import com.platform.general.microservice.web.credential.config.ConstantaAAAA;
import com.platform.general.microservice.web.credential.exceptions.InvalidArgumentException;
import com.platform.general.microservice.web.credential.ports.out.WebCredentialRepository;
import com.platform.general.microservice.web.credential.validators.PasswordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class WebCredentialUpdater implements IWebCredentialUpdater {


    private final WebCredentialRepository repository;

    private final PasswordValidator validator;


    @Autowired
    public WebCredentialUpdater(@Qualifier(ConstantaAAAA.QUALIFIER_WEB_CREDENTIAL_REPOSITORY) WebCredentialRepository repository, PasswordValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    public void updatePassword(String password, UUID credentialId){
        if(!validator.isValid(password)){
            throw new InvalidArgumentException(InvalidArgumentException.Error.PASSWORD_INVALID_STRUCTURE);
        }
        repository.updatePassword(password,credentialId);
    }

}
