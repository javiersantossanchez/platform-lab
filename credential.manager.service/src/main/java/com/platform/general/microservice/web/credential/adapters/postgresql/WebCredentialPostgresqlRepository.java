package com.platform.general.microservice.web.credential.adapters.postgresql;

import com.platform.general.microservice.web.credential.WebCredential;
import com.platform.general.microservice.web.credential.exceptions.*;
import com.platform.general.microservice.web.credential.exceptions.IllegalArgumentException;
import com.platform.general.microservice.web.credential.ports.out.WebCredentialRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service("postgresql")
public class WebCredentialPostgresqlRepository implements WebCredentialRepository {

    private WebCredentialDao dao;

    @Autowired
    public WebCredentialPostgresqlRepository(WebCredentialDao dao) {
        this.dao = dao;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Retryable(value = { WebCredentialRegistrationException.class }, maxAttempts = 3, backoff = @Backoff(delay = 3000))
    public WebCredential save(String password, String userName, String credentialName, LocalDateTime creationDate, UUID userId) {

        WebCredentialEntity entity = WebCredentialEntity.builder()
                        .password(password)
                        .userName(userName)
                        .credentialName(credentialName)
                        .creationTime(creationDate)
                        .userId(userId)
                        .build();
        WebCredentialEntity newEntity = null;
        try {
            newEntity = dao.save(entity);
        }catch(DataIntegrityViolationException ex){
            if(ex.getCause() instanceof ConstraintViolationException){
                ConstraintViolationException cause = (ConstraintViolationException) ex.getCause();
                if(cause.getSQLState().equals("23505") && cause.getConstraintName().equals("user_name_unique")){
                    throw new IllegalArgumentException(IllegalArgumentException.Argument.USER_NAME, IllegalArgumentException.Validation.DUPLICATED);
                }
            }
            throw new WebCredentialRegistrationException(ex);
        } catch (RuntimeException ex){
            throw new WebCredentialRegistrationException(ex);
        }
        return buildWebCredential(newEntity);
    }

    /**
     * TODO: review the retry functionality
     *
     * {@inheritDoc}
     */
    @Override
    public WebCredential findById(UUID id) {
        if(id == null){
            throw new IllegalArgumentException(IllegalArgumentException.Argument.ID, IllegalArgumentException.Validation.NOT_EMPTY);
        }
        WebCredentialEntity newEntity = null;
        try {
            newEntity = dao.findById(id).orElse(null);
        }catch (RuntimeException ex){
            throw new WebCredentialSearchException(ex);
        }
        if(newEntity == null){
            throw new WebCredentialNotFoundException();
        }
        return buildWebCredential(newEntity);
    }

    /**
     * TODO: review the retry functionality
     *
     * {@inheritDoc}
     */
    @Override
    @Retryable(value = { WebCredentialDeleteException.class }, maxAttempts = 3, backoff = @Backoff(delay = 3000))
    public void deleteById(UUID id) {
        if(id == null){
            throw new IllegalArgumentException(IllegalArgumentException.Argument.ID, IllegalArgumentException.Validation.NOT_EMPTY);
        }
        try {
            dao.deleteById(id);
        }catch(EmptyResultDataAccessException ex){
            throw new WebCredentialNotFoundException(ex);
        }catch (RuntimeException ex){
            throw new WebCredentialDeleteException(ex);
        }
    }

    private WebCredential buildWebCredential(WebCredentialEntity entity){
        if(entity == null){
            return null;
        }
        WebCredential webCredential = new WebCredential();
        webCredential.setPassword(entity.getPassword());
        webCredential.setUserName(entity.getUserName());
        webCredential.setCreationDate(entity.getCreationTime());
        webCredential.setId(entity.getId());
        webCredential.setCredentialName(entity.getCredentialName());
        return webCredential;
    }
}
