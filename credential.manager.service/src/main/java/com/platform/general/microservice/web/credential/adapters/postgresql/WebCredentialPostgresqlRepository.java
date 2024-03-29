package com.platform.general.microservice.web.credential.adapters.postgresql;

import com.platform.general.microservice.web.credential.WebCredential;
import com.platform.general.microservice.web.credential.exceptions.*;
import com.platform.general.microservice.web.credential.exceptions.IllegalArgumentException;
import com.platform.general.microservice.web.credential.ports.out.WebCredentialRepository;
import com.platform.general.microservice.web.credential.utils.PagingContext;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.postgresql.util.PSQLException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.platform.general.microservice.web.credential.exceptions.InvalidArgumentException.Error.PASSWORD_SIZE_BIGGER_THAN_VALUE_ALLOWS;
import static org.postgresql.util.PSQLState.NOT_NULL_VIOLATION;

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
            throw new WebCredentialServiceNotAvailableException(ErrorMessageConstants.SEARCH_SERVICE_NOT_AVAILABLE,ex);
        }catch (RuntimeException ex){
            throw new WebCredentialGeneralException(ErrorMessageConstants.SEARCHING_CREDENTIAL_FAILS,ex);
        }
        if(newEntity == null){//TODO: review this error, Do I want to throws a error on this point?
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
            throw new WebCredentialGeneralException(ex);
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


    @Override
    public int updatePassword(String newPassword, UUID credentialId) {
        if(newPassword!= null && newPassword.isBlank()){
            throw new InvalidArgumentException(InvalidArgumentException.Error.PASSWORD_SHOULD_BE_NOT_EMPTY);
        }
        if(credentialId == null){
            throw new InvalidArgumentException(InvalidArgumentException.Error.CREDENTIAL_ID_SHOULD_BE_NOT_NULL);
        }
        try {
            return dao.updatePassword(newPassword,credentialId);
        }catch(DataIntegrityViolationException ex){
            if(ex.getCause() instanceof DataException){
                DataException dataException = (DataException) ex.getCause();
                if(dataException.getCause() instanceof PSQLException) {
                    PSQLException psqlException = (PSQLException) dataException.getCause();
                    if("22001".equals(psqlException.getSQLState()) && "ERROR: value too long for type character varying(50)".equals(psqlException.getMessage())){
                        throw new InvalidArgumentException(PASSWORD_SIZE_BIGGER_THAN_VALUE_ALLOWS);
                    }
                }
            }else if(ex.getCause() instanceof ConstraintViolationException){
                ConstraintViolationException constraintViolationException = (ConstraintViolationException) ex.getCause();
                if(constraintViolationException.getCause() instanceof PSQLException) {
                    PSQLException psqlException = (PSQLException) constraintViolationException.getCause();
                    if(NOT_NULL_VIOLATION.getState().equals(psqlException.getSQLState()) && "ERROR: null value in column \"password\" of relation \"user_password_credential\" violates not-null constraint".startsWith(psqlException.getMessage())){
                        throw new InvalidArgumentException(InvalidArgumentException.Error.PASSWORD_SHOULD_BE_NOT_NULL);
                    }
                }
            }
            throw new WebCredentialUpdateException(ex);
        } catch (RuntimeException ex){
            throw new WebCredentialUpdateException(ex);
        }
    }


////////////////////////////////////////////////////////////////////
///////////////////////INTERNAL METHODS/////////////////////////////
///////////////////////////////////////////////////////////////////
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