package com.platform.general.microservice.web.credential.worker;

import com.github.javafaker.Faker;
import com.platform.general.microservice.web.credential.WebCredential;
import com.platform.general.microservice.web.credential.exceptions.InvalidArgumentException;
import com.platform.general.microservice.web.credential.exceptions.WebCredentialGeneralException;
import com.platform.general.microservice.web.credential.exceptions.WebCredentialNotFoundException;
import com.platform.general.microservice.web.credential.ports.out.WebCredentialRepository;
import com.platform.general.microservice.web.credential.utils.PagingContext;
import com.platform.general.microservice.web.credential.validators.PasswordValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class WebCredentialUpdaterTest {

    private final Faker faker = new Faker();

    @InjectMocks
    WebCredentialUpdater target;

    @Mock
    WebCredentialRepository repository;

    @Mock
    PasswordValidator validator;

    @Test
    public void updatePasswordWhenOk(){
        UUID credentialId = UUID.randomUUID();
        String password = faker.internet().password();
        Mockito.doReturn(true).when(validator).isValid(password);
        Mockito.doReturn(1).when(repository).updatePassword(password,credentialId);
        target.updatePassword(password,credentialId);
        Mockito.verify(repository,Mockito.times(1)).updatePassword(password,credentialId);
    }

    @Test
    public void updatePasswordWithInvalidPassword(){
        UUID credentialId = UUID.randomUUID();
        String password = "invalid-password";
        Mockito.doReturn(false).when(validator).isValid(password);
        InvalidArgumentException exception = Assertions.assertThrows(InvalidArgumentException.class,()->target.updatePassword(password,credentialId));
        Assertions.assertEquals(InvalidArgumentException.Error.PASSWORD_INVALID_STRUCTURE,exception.getError());
    }



}
