package com.platform.general.microservice.web.credential;

import com.platform.general.microservice.web.credential.config.ConstantaAAAA;
import com.platform.general.microservice.web.credential.ports.out.WebCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class WebCredentialDeleter {

    @Autowired
    @Qualifier(ConstantaAAAA.QUALIFIER_WEB_CREDENTIAL_REPOSITORY)
    WebCredentialRepository repository;

    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

}
