package com.platform.general.microservice.web.credential.worker;


import com.platform.general.microservice.web.credential.WebCredential;
import com.platform.general.microservice.web.credential.config.ConstantaAAAA;
import com.platform.general.microservice.web.credential.exceptions.IllegalArgumentException;
import com.platform.general.microservice.web.credential.exceptions.InvalidPasswordException;
import com.platform.general.microservice.web.credential.ports.out.WebCredentialRepository;
import com.platform.general.microservice.web.credential.utils.DateManager;
import com.platform.general.microservice.web.credential.validators.PasswordValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class WebCredentialCreator implements IWebCredentialCreator {


    private final WebCredentialRepository repository;

    private final PasswordValidator validator;

    private final DateManager dateManager;

    @Autowired
    public WebCredentialCreator(@Qualifier(ConstantaAAAA.QUALIFIER_WEB_CREDENTIAL_REPOSITORY) WebCredentialRepository repository, PasswordValidator validator, DateManager dateManager) {
        this.repository = repository;
        this.validator = validator;
        this.dateManager = dateManager;
    }

    @Override
    public WebCredential create(String password, String userName, String webSite, UUID userId) {

        if(StringUtils.isBlank(userName)){
            throw new IllegalArgumentException(IllegalArgumentException.Argument.USER_NAME, IllegalArgumentException.Validation.NOT_EMPTY);
        }
        if(StringUtils.isBlank(webSite)){
            throw new IllegalArgumentException(IllegalArgumentException.Argument.WEB_SITE, IllegalArgumentException.Validation.NOT_EMPTY);
        }
        if(userId == null){
            throw new IllegalArgumentException(IllegalArgumentException.Argument.USER_ID, IllegalArgumentException.Validation.NOT_EMPTY);
        }
        if(!validator.isValid(password)) {
            throw new InvalidPasswordException();
        }

        return repository.save(password, userName, webSite,dateManager.getCurrentLocalDate(),userId);
    }
}
