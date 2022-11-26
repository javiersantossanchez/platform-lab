package com.platform.general.microservice.web.credential.adapters.postgresql;

import com.github.javafaker.Faker;
import com.platform.general.microservice.web.credential.WebCredential;
import com.platform.general.microservice.web.credential.exceptions.*;
import com.platform.general.microservice.web.credential.exceptions.IllegalArgumentException;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class WebCredentialPostgresqlRepositoryUnitTest {

    private final Faker faker = new Faker();

    @InjectMocks
    private WebCredentialPostgresqlRepository target;

    @Mock
    private WebCredentialDao repo;


    @Test()
    public void createOneCredentialWhenOk(){
        String userName = faker.name().username();
        String credentialName = faker.internet().domainName();
        String password = faker.internet().password();
        LocalDateTime creationDate =  LocalDateTime.now();
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        WebCredentialEntity entity =  WebCredentialEntity
                .builder()
                .userName(userName)
                .password(password)
                .credentialName(credentialName)
                .creationTime(creationDate)
                .userId(userId)
                .build();
        WebCredentialEntity entityCreated =  WebCredentialEntity
                .builder()
                .id(id)
                .userName(userName)
                .password(password)
                .credentialName(credentialName)
                .creationTime(creationDate)
                .userId(userId)
                .build();

        Mockito.doReturn(entityCreated).when(repo).save(entity);

        WebCredential result = target.save(password, userName, credentialName,creationDate,userId);

        Mockito.verify(repo,Mockito.times(1)).save(entity);
        Assertions.assertEquals(entityCreated.getPassword(),result.getPassword());
        Assertions.assertEquals(entityCreated.getUserName(),result.getUserName());
        Assertions.assertEquals(entityCreated.getCreationTime(),result.getCreationDate());
        Assertions.assertEquals(entityCreated.getCredentialName(),result.getCredentialName());

        Assertions.assertEquals(entityCreated.getId(),result.getId());
    }

    @Test()
    public void createOneCredentialWhenDuplicatedUserName(){
        String userName = faker.name().username();
        String credentialName = faker.internet().domainName();
        String password = faker.internet().password();
        LocalDateTime creationDate =  LocalDateTime.now();
        UUID userId = UUID.randomUUID();
        WebCredentialEntity entity =  WebCredentialEntity
                .builder()
                .userName(userName)
                .password(password)
                .credentialName(credentialName)
                .creationTime(creationDate)
                .userId(userId)
                .build();

        SQLException sqlEx = new SQLException(faker.friends().quote(),"23505");
        ConstraintViolationException cex = new ConstraintViolationException(faker.friends().quote(),sqlEx,"user_name_unique");
        Mockito.doThrow(new DataIntegrityViolationException(faker.friends().quote(),cex)).when(repo).save(entity);

        Assertions.assertThrows(IllegalArgumentException.class,()->target.save(password, userName, credentialName,creationDate,userId));

    }

    @Test()
    public void createOneCredentialWhenDatabaseErrorIsThrown(){
        String userName = faker.name().username();
        String credentialName = faker.internet().domainName();
        String password = faker.internet().password();
        LocalDateTime creationDate = LocalDateTime.now();
        UUID userId = UUID.randomUUID();
        WebCredentialEntity entity = WebCredentialEntity.builder()
                .password(password)
                .userName(userName)
                .credentialName(credentialName)
                .creationTime(creationDate)
                .userId(userId)
                .build();
        Mockito.doThrow(RuntimeException.class).when(repo).save(entity);

        Assertions.assertThrows(WebCredentialRegistrationException.class,()->target.save(password, userName, credentialName,creationDate,userId));
        Mockito.verify(repo,Mockito.times(1)).save(entity);
    }


    ///////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////
    @Test()
    public void findCredentialByIDWithNull(){
        Assertions.assertThrows(IllegalArgumentException.class,()->{target.findById(null);});
    }

    @Test()
    public void findCredentialByIDWhenIdDoesNotExist(){
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Mockito.doReturn(Optional.ofNullable(null)).when(repo).findOneByIdAndUserId(id,userId);
        Assertions.assertThrows(WebCredentialNotFoundException.class,()->target.findById(id,userId));
    }

    @Test()
    public void findCredentialByIDWhenDatabaseErrorIsThrown(){
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Mockito.doThrow(RuntimeException.class).when(repo).findOneByIdAndUserId(id,userId);

        Assertions.assertThrows(WebCredentialSearchException.class,()->target.findById(id,userId));
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////
    @Test()
    public void deleteCredentialByIDWithNull(){
        Assertions.assertThrows(IllegalArgumentException.class,()->{target.deleteById(null);});
    }

    @Test()
    public void deleteCredentialByIDWhenIdDoesNotExist(){
        UUID id = UUID.randomUUID();
        Mockito.doThrow(EmptyResultDataAccessException.class).when(repo).deleteById(id);
        Assertions.assertThrows(WebCredentialNotFoundException.class,()-> target.deleteById(id) );
    }

    @Test()
    public void deleteCredentialByIDWhenDatabaseErrorIsThrown(){
        UUID id = UUID.randomUUID();
        Mockito.doThrow(RuntimeException.class).when(repo).deleteById(id);

        Assertions.assertThrows(WebCredentialDeleteException.class,()->target.deleteById(id));
    }

}
