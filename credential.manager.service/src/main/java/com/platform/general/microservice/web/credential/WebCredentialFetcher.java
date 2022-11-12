package com.platform.general.microservice.web.credential;

import com.platform.general.microservice.web.credential.config.ConstantaAAAA;
import com.platform.general.microservice.web.credential.ports.out.WebCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class WebCredentialFetcher   {

    private WebCredentialRepository repository;

    @Autowired
    public WebCredentialFetcher(@Qualifier(ConstantaAAAA.QUALIFIER_WEB_CREDENTIAL_REPOSITORY) WebCredentialRepository repository) {
        this.repository = repository;
    }
    

    public WebCredential findById(UUID id){
        return repository.findById(id);
    }
}
