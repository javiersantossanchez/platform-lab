package com.platform.general.microservice.web.credential.adapters.postgresql;

import com.github.javafaker.Faker;
import com.platform.general.microservice.web.credential.exceptions.WebCredentialRegistrationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

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
        WebCredentialEntity entity = new WebCredentialEntity(password,userName,credentialName);
        Mockito.doReturn(entity).when(repo).save(entity);

        target.save(password, userName, credentialName,null);

        Mockito.verify(repo,Mockito.times(1)).save(entity);
    }

    @Test()
    public void createOneCredentialWhenDatabaseErrorIsThrown(){
        String userName = faker.name().username();
        String credentialName = faker.internet().domainName();
        String password = faker.internet().password();
        WebCredentialEntity entity = new WebCredentialEntity(password,userName,credentialName);
        Mockito.doThrow(RuntimeException.class).when(repo).save(entity);

        Assertions.assertThrows(WebCredentialRegistrationException.class,()->{target.save(password, userName, credentialName,null);});
    }


}
