package com.platform.general.microservice.web.credential.adapters.postgresql;

import com.platform.general.microservice.web.credential.WebCredential;
import com.platform.general.microservice.web.credential.exceptions.*;
import com.platform.general.microservice.web.credential.exceptions.IllegalArgumentException;
import com.platform.general.microservice.web.credential.ports.out.WebCredentialRepository;
import com.platform.general.microservice.web.credential.utils.PagingContext;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service("postgresql")
public class WebCredentialPostgresqlRepository implements WebCredentialRepository {

    private final WebCredentialDao dao;

    private final WebCredentialPostgresqlRepositoryResilient repositoryResilient;

    @Autowired
    public WebCredentialPostgresqlRepository(final WebCredentialDao dao,final WebCredentialPostgresqlRepositoryResilient repositoryResilient) {
        this.dao = dao;
        this.repositoryResilient = repositoryResilient;
    }

    /**
     * {@inheritDoc}
     * TODO: review the retry functionality
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
     * {@inheritDoc}
     */
    @Override
    public WebCredential findById(final UUID credentialId,final UUID userId) {

        WebCredentialEntity newEntity = null;
        try {
            newEntity = repositoryResilient.find(credentialId, userId);
        }catch(CallNotPermittedException ex){
            throw new WebCredentialServiceNotAvailableException(ex);
        }catch (RuntimeException ex){
            throw new WebCredentialSearchException(ex);
        }
        if(newEntity == null){
            throw new WebCredentialNotFoundException();
        }
        return buildWebCredential(newEntity);
    }


    @Override
    public List<WebCredential> findById(final UUID userId, PagingContext paging) {

        if(paging == null){
            throw new EmptyPagingParameterException();
        }
        if(paging.getPageNumber() < 0){
            throw new InvalidArgumentException(InvalidArgumentException.Error.PAGE_NUMBER_ON_PAGING_SHOULD_BE_BIGGER_THAN_ZERO);
        }
        if(paging.getPageSize()< 1){
            throw new InvalidArgumentException(InvalidArgumentException.Error.PAGE_SIZE_ON_PAGING_SHOULD_BE_BIGGER_THAN_ZERO);
        }

        Pageable pageable = PageRequest.of(paging.getPageNumber(), paging.getPageSize(), Sort.by("creationTime").descending());

        List<WebCredentialEntity> newEntity = new ArrayList<>();
        try {
            newEntity = repositoryResilient.findByUserId(userId,pageable);
        }catch(CallNotPermittedException ex){
            throw new WebCredentialServiceNotAvailableException(ex);
        }catch (RuntimeException ex){
            throw new WebCredentialSearchException(ex);
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


    private List<WebCredential> buildWebCredential(List<WebCredentialEntity> entityList){
        return entityList.stream().map(current -> this.buildWebCredential(current)).collect(Collectors.toList());
    }
    private WebCredential buildWebCredential(WebCredentialEntity entity){
        if(entity == null){
            return null;
        }

        return  WebCredential
                .builder()
                .password(entity.getPassword())
                .userName(entity.getUserName())
                .creationDate(entity.getCreationTime())
                .credentialName(entity.getCredentialName())
                .id(entity.getId())
                .userId(entity.getUserId())
                .build();
    }
}