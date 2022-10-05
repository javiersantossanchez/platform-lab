package com.platform.general.microservice.web.credential;

import com.github.javafaker.Faker;
import com.platform.general.microservice.web.credential.exceptions.InvalidPasswordException;
import com.platform.general.microservice.web.credential.ports.out.WebCredentialRepository;
import com.platform.general.microservice.web.credential.validators.PasswordValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class WebCredentialCreatorTest {

    private final Faker faker = new Faker();

    @InjectMocks
    WebCredentialCreator target;

    @Mock
    PasswordValidator validator;

    @Mock
    WebCredentialRepository repository;

    @Test()
    public void createWithInvalidPassword(){
        String password = faker.internet().password();
        Mockito.doReturn(false).when(validator).isValid(password);
        Assertions.assertThrows(InvalidPasswordException.class, () -> {
            target.create(password,faker.name().username(),faker.internet().domainName());
        });
    }

    @Test()
    public void createWhenOK(){
        String password = faker.internet().password();
        String userName = faker.name().username();
        String webSite = faker.internet().domainName();

        Mockito.doReturn(true).when(validator).isValid(password);
        Mockito.doReturn(new WebCredential()).when(repository).save(password,userName,webSite);
        WebCredential result = target.create(password,userName,webSite);
        Mockito.verify(repository,Mockito.times(1)).save(password,userName,webSite);
        Assertions.assertNotNull(result);
    }

}
