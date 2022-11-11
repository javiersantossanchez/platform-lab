package com.platform.general.microservice.web.credential.adapters.postgresql;

import com.platform.general.microservice.web.credential.WebCredential;
import com.platform.general.microservice.web.credential.exceptions.AuditEventRegistrationException;
import com.platform.general.microservice.web.credential.exceptions.WebCredentialRegistrationException;
import com.platform.general.microservice.web.credential.ports.out.WebCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service("postgresql")
public class WebCredentialPostgresqlRepository implements WebCredentialRepository {

    private WebCredentialDao dao;

    @Autowired
    public WebCredentialPostgresqlRepository(WebCredentialDao dao) {
        this.dao = dao;
    }

    @Override
    @Retryable(value = { WebCredentialRegistrationException.class }, maxAttempts = 3, backoff = @Backoff(delay = 3000))
    public WebCredential save(String password, String userName, String credentialName, LocalDateTime creationDate) {

        WebCredentialEntity entity = new WebCredentialEntity(password,userName,credentialName);
        WebCredentialEntity newEntity = null;
        try {
            newEntity = dao.save(entity);
        }catch (Exception ex){
            throw new WebCredentialRegistrationException(ex);
        }
        WebCredential newCredential = new WebCredential();
        newCredential.setPassword(newEntity.getPassword());
        newCredential.setUserName(newEntity.getUserName());
        //newCredential.setWebSite(credentialName);
        //newCredential.setCreationDate(creationDate);
        //newCredential.setId(id);
        return newCredential;
    }

    @Override
    public List<WebCredential> findAll() {
        return null;
    }

    /**
     * @return 
     */
    @Override
    public WebCredential findById(UUID id) {
        return null;
    }

    /**
     * @param id
     */
    @Override
    public void deleteById(UUID id) {
    }
}
