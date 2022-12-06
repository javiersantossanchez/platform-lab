package com.platform.general.microservice.web.credential;

import com.platform.general.microservice.web.credential.config.ConstantaAAAA;
import com.platform.general.microservice.web.credential.exceptions.EmptyUserIdException;
import com.platform.general.microservice.web.credential.exceptions.IllegalArgumentException;
import com.platform.general.microservice.web.credential.exceptions.InvalidUserInformationException;
import com.platform.general.microservice.web.credential.ports.out.WebCredentialRepository;
import com.platform.general.microservice.web.credential.utils.PagingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class WebCredentialFetcherImpl implements WebCredentialFetcher {

    private final WebCredentialRepository repository;

    @Autowired
    public WebCredentialFetcherImpl(@Qualifier(ConstantaAAAA.QUALIFIER_WEB_CREDENTIAL_REPOSITORY) final WebCredentialRepository repository) {
        this.repository = repository;
    }

    @Override
    public WebCredential findById(final UUID id, final UUID userId) {
        if(userId == null){
            throw new EmptyUserIdException();
        }
        if(id == null){
            throw new IllegalArgumentException(IllegalArgumentException.Argument.ID, IllegalArgumentException.Validation.NOT_EMPTY);
        }
        return repository.findById(id,userId);
    }

    @Override
    public List<WebCredential> findByUserId(final UUID userId, final PagingContext paging) {
        if(userId == null){
            throw new InvalidUserInformationException();
        }
        return repository.findById(userId,paging);
    }
}
