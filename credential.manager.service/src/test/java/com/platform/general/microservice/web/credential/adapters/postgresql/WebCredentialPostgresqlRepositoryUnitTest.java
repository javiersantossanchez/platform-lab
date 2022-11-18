package com.platform.general.microservice.web.credential.adapters.postgresql;

import com.github.javafaker.Faker;
import com.platform.general.microservice.web.credential.WebCredential;
import com.platform.general.microservice.web.credential.exceptions.*;
import com.platform.general.microservice.web.credential.exceptions.IllegalArgumentException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

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
        WebCredentialEntity entity =  WebCredentialEntity
                .builder()
                .userName(userName)
                .password(password)
                .credentialName(credentialName)
                .creationTime(creationDate)
                .build();
        WebCredentialEntity entityCreated =  WebCredentialEntity
                .builder()
                .id(id)
                .userName(userName)
                .password(password)
                .credentialName(credentialName)
                .creationTime(creationDate)
                .build();

        Mockito.doReturn(entityCreated).when(repo).save(entity);

        WebCredential result = target.save(password, userName, credentialName,creationDate);

        Mockito.verify(repo,Mockito.times(1)).save(entity);
        Assertions.assertEquals(entityCreated.getPassword(),result.getPassword());
        Assertions.assertEquals(entityCreated.getUserName(),result.getUserName());
        Assertions.assertEquals(entityCreated.getCreationTime(),result.getCreationDate());

        Assertions.assertEquals(entityCreated.getId(),result.getId());
    }

    @Test()
    public void createOneCredentialWhenDatabaseErrorIsThrown(){
        String userName = faker.name().username();
        String credentialName = faker.internet().domainName();
        String password = faker.internet().password();
        LocalDateTime creationDate = LocalDateTime.now();
        WebCredentialEntity entity = new WebCredentialEntity(password,userName,credentialName,creationDate);
        Mockito.doThrow(RuntimeException.class).when(repo).save(entity);

        Assertions.assertThrows(WebCredentialRegistrationException.class,()->{target.save(password, userName, credentialName,creationDate);});
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
        Mockito.doReturn(Optional.ofNullable(null)).when(repo).findById(id);
        Assertions.assertThrows(WebCredentialNotFoundException.class,()->target.findById(id));
    }

    @Test()
    public void findCredentialByIDWhenDatabaseErrorIsThrown(){
        UUID id = UUID.randomUUID();
        Mockito.doThrow(RuntimeException.class).when(repo).findById(id);

        Assertions.assertThrows(WebCredentialSearchException.class,()->{target.findById(id);});
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
        UUID id = UUID.randomUUID();findCredentialByIDWhenIdDoesNotExist();
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
