package com.platform.general.microservice.web.credential;

import com.platform.general.microservice.web.credential.config.ConstantaAAAA;
import com.platform.general.microservice.web.credential.ports.out.WebCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class WebCredentialFetcherImpl implements WebCredentialFetcher {

    private final WebCredentialRepository repository;

    @Autowired
    public WebCredentialFetcherImpl(@Qualifier(ConstantaAAAA.QUALIFIER_WEB_CREDENTIAL_REPOSITORY) final WebCredentialRepository repository) {
        this.repository = repository;
    }


    @Override
    public WebCredential findById(final UUID id) {
        return repository.findById(id);
    }
}
