package com.platform.general.microservice.web.credential;

import com.github.javafaker.Faker;
import com.platform.general.microservice.web.credential.exceptions.IllegalArgumentException;
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

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@ExtendWith(MockitoExtension.class)
public class WebCredentialImplTest {

    private final Faker faker = new Faker();

    @InjectMocks
    WebCredentialImpl target;

    @Mock
    IWebCredentialCreator creator;

    @Mock
    WebCredentialFetcher fetcher;

    @Mock
    WebCredentialDeleter deleter;

    @Mock
    AuditEventRegister auditEventRegister;

    @Test
    public void createNewCredentialWhenOK(){

        WebCredential newCredential = new WebCredential();
        newCredential.setPassword(faker.internet().password());
        newCredential.setUserName(faker.name().username());
        newCredential.setCredentialName(faker.internet().domainName());
        newCredential.setId(UUID.randomUUID());
        Mockito.doReturn(newCredential).when(creator).create(newCredential.getPassword(),newCredential.getUserName(),newCredential.getCredentialName());
        Mockito.doReturn(CompletableFuture.completedFuture(AuditEvent.builder().build())).when(auditEventRegister).register(AuditEvent.AuditEventType.CREATE_CREDENTIAL);

        WebCredential credentialCreated = target.createNewWebCredential(newCredential.getPassword(), newCredential.getUserName(), newCredential.getCredentialName());
        Assertions.assertEquals(newCredential.getPassword(),credentialCreated.getPassword());
        Assertions.assertEquals(newCredential.getUserName(),credentialCreated.getUserName());
        Assertions.assertEquals(newCredential.getCredentialName(),credentialCreated.getCredentialName());
        Assertions.assertNotNull(credentialCreated.getId());

        Mockito.verify(creator,Mockito.times(1)).create(newCredential.getPassword(), newCredential.getUserName(), newCredential.getCredentialName());
        Mockito.verify(auditEventRegister,Mockito.times(1)).register(AuditEvent.AuditEventType.CREATE_CREDENTIAL);
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    public void createNewCredentialWhenEmptyPassword(String password){

        WebCredential newCredential = new WebCredential();
        newCredential.setPassword(password);
        newCredential.setUserName(faker.name().username());
        newCredential.setCredentialName(faker.internet().domainName());
        newCredential.setId(UUID.randomUUID());

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,()->{target.createNewWebCredential(newCredential.getPassword(), newCredential.getUserName(), newCredential.getCredentialName());});
        Assertions.assertEquals(IllegalArgumentException.Argument.PASSWORD,exception.getArgument());
        Assertions.assertEquals(IllegalArgumentException.Validation.NOT_EMPTY,exception.getValidationFailed());
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    public void createNewCredentialWhenEmptyUserName(String userName){

        WebCredential newCredential = new WebCredential();
        newCredential.setPassword(faker.internet().password());
        newCredential.setUserName(userName);
        newCredential.setCredentialName(faker.internet().domainName());
        newCredential.setId(UUID.randomUUID());

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,()->{target.createNewWebCredential(newCredential.getPassword(), newCredential.getUserName(), newCredential.getCredentialName());});
        Assertions.assertEquals(IllegalArgumentException.Argument.USER_NAME,exception.getArgument());
        Assertions.assertEquals(IllegalArgumentException.Validation.NOT_EMPTY,exception.getValidationFailed());
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    public void createNewCredentialWhenEmptyWebSite(String webSite){

        WebCredential newCredential = new WebCredential();
        newCredential.setPassword(faker.internet().password());
        newCredential.setUserName(faker.name().username());
        newCredential.setCredentialName(webSite);
        newCredential.setId(UUID.randomUUID());

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,()->{target.createNewWebCredential(newCredential.getPassword(), newCredential.getUserName(), newCredential.getCredentialName());});
        Assertions.assertEquals(IllegalArgumentException.Argument.WEB_SITE,exception.getArgument());
        Assertions.assertEquals(IllegalArgumentException.Validation.NOT_EMPTY,exception.getValidationFailed());
    }

    @Test
    public void findCredentialWhenOk(){
        WebCredential newCredential = new WebCredential();
        newCredential.setPassword(faker.internet().password());
        newCredential.setUserName(faker.name().username());
        newCredential.setCredentialName(faker.internet().domainName());
        newCredential.setId(UUID.randomUUID());
        Mockito.doReturn(newCredential).when(fetcher).findById(newCredential.getId());
        WebCredential credentialFound = target.findById(newCredential.getId());
        Assertions.assertEquals(newCredential,credentialFound);
    }

    @Test
    public void deleteCredentialWhenOk(){
        UUID idToDelete = UUID.randomUUID();
        Mockito.doNothing().when(deleter).deleteById(idToDelete);
        target.deleteByID(idToDelete);
        Mockito.verify(deleter,Mockito.times(1)).deleteById(idToDelete);
    }
}
