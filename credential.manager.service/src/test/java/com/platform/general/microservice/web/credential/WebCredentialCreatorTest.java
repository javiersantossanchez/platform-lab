package com.platform.general.microservice.web.credential;

import com.github.javafaker.Faker;
import com.platform.general.microservice.web.credential.exceptions.IllegalArgumentException;
import com.platform.general.microservice.web.credential.exceptions.InvalidPasswordException;
import com.platform.general.microservice.web.credential.exceptions.WebCredentialRegistrationException;
import com.platform.general.microservice.web.credential.ports.out.WebCredentialRepository;
import com.platform.general.microservice.web.credential.utils.DateManager;
import com.platform.general.microservice.web.credential.validators.PasswordValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class WebCredentialCreatorTest {

    private final Faker faker = new Faker();

    @InjectMocks
    WebCredentialCreator target;

    @Mock
    PasswordValidator validator;

    @Mock
    WebCredentialRepository repository;

    @Mock
    DateManager dateManager;

    @Test()
    public void createWithInvalidPassword(){
        String password = faker.internet().password();
        Mockito.doReturn(false).when(validator).isValid(password);
        Assertions.assertThrows(InvalidPasswordException.class, () -> target.create(password,faker.name().username(),faker.internet().domainName()));
    }

    @Test()
    public void createWhenOK(){
        String password = faker.internet().password();
        String userName = faker.name().username();
        String webSite = faker.internet().domainName();
        UUID userId = UUID.randomUUID();
        LocalDateTime now =  LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);

        Mockito.doReturn(true).when(validator).isValid(password);
        Mockito.doReturn(now).when(dateManager).getCurrentLocalDate();

        Mockito.doReturn(new WebCredential()).when(repository).save(password,userName,webSite,now,userId);
        WebCredential result = target.create(password,userName,webSite,userId);
        Mockito.verify(repository,Mockito.times(1)).save(password,userName,webSite,now,userId);
        Assertions.assertNotNull(result);
    }

    @Test()
    public void createWhenAnExceptionIsThrownCreatingCredential(){
        String password = faker.internet().password();
        String userName = faker.name().username();
        String webSite = faker.internet().domainName();
        UUID userId = UUID.randomUUID();
        LocalDateTime now =  LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);

        Mockito.doReturn(true).when(validator).isValid(password);
        Mockito.doReturn(now).when(dateManager).getCurrentLocalDate();

        Mockito.doThrow(WebCredentialRegistrationException.class).when(repository).save(password,userName,webSite,now,userId);
        Assertions.assertThrows(WebCredentialRegistrationException.class,()-> target.create(password,userName,webSite,userId));
    }

    @Test()
    public void createWhenEmptyUserID(){
        String userName = faker.name().username();
        String webSite = faker.internet().domainName();
        String password = faker.internet().password();
        UUID userId =null;
        LocalDateTime now =  LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,()-> target.create(password,userName,webSite,userId));
        Assertions.assertEquals(IllegalArgumentException.Argument.USER_ID,exception.getArgument());
        Assertions.assertEquals(IllegalArgumentException.Validation.NOT_EMPTY,exception.getValidationFailed());
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    public void createWhenEmptyPassword(String password){
        String userName = faker.name().username();
        String webSite = faker.internet().domainName();

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,()-> target.create(password,userName,webSite));
        Assertions.assertEquals(IllegalArgumentException.Argument.PASSWORD,exception.getArgument());
        Assertions.assertEquals(IllegalArgumentException.Validation.NOT_EMPTY,exception.getValidationFailed());
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    public void createWhenEmptyUserName(String userName ){
        String password = faker.internet().password();
        String webSite = faker.internet().domainName();

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,()-> target.create(password,userName,webSite));
        Assertions.assertEquals(IllegalArgumentException.Argument.USER_NAME,exception.getArgument());
        Assertions.assertEquals(IllegalArgumentException.Validation.NOT_EMPTY,exception.getValidationFailed());
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    public void createWhenEmptyWebSite(String webSite ){
        String password = faker.internet().password();
        String userName = faker.name().username();

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,()-> target.create(password,userName,webSite));
        Assertions.assertEquals(IllegalArgumentException.Argument.WEB_SITE,exception.getArgument());
        Assertions.assertEquals(IllegalArgumentException.Validation.NOT_EMPTY,exception.getValidationFailed());
    }
}
