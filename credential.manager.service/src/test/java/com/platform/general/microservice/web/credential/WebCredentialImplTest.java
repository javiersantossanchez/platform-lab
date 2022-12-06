package com.platform.general.microservice.web.credential;

import com.github.javafaker.Faker;
import com.platform.general.microservice.web.credential.exceptions.IllegalArgumentException;
import com.platform.general.microservice.web.credential.test.utils.WebCredentialMother;
import com.platform.general.microservice.web.credential.utils.PagingContext;
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

import java.util.List;
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
        String password = faker.internet().password();
        String userName = faker.name().username();
        String credentialName = faker.internet().domainName();
        UUID userId = UUID.randomUUID();

        WebCredential newCredential = WebCredential.builder()
                .password(password)
                .userName(userName)
                .credentialName(credentialName)
                .id(UUID.randomUUID())
                .build();

        Mockito.doReturn(newCredential).when(creator).create(password,userName,credentialName,userId);
        Mockito.doReturn(CompletableFuture.completedFuture(AuditEvent.builder().build())).when(auditEventRegister).register(AuditEvent.AuditEventType.CREATE_CREDENTIAL);

        WebCredential credentialCreated = target.createNewWebCredential(password,userName,credentialName,userId);
        Assertions.assertEquals(newCredential.getPassword(),credentialCreated.getPassword());
        Assertions.assertEquals(newCredential.getUserName(),credentialCreated.getUserName());
        Assertions.assertEquals(newCredential.getCredentialName(),credentialCreated.getCredentialName());
        Assertions.assertNotNull(credentialCreated.getId());

        Mockito.verify(creator,Mockito.times(1)).create(password,userName,credentialName,userId);
        Mockito.verify(auditEventRegister,Mockito.times(1)).register(AuditEvent.AuditEventType.CREATE_CREDENTIAL);
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    public void createNewCredentialWhenEmptyUserName(String userName){

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,()->target.createNewWebCredential(faker.internet().password(), userName, faker.internet().domainName(),UUID.randomUUID()));
        Assertions.assertEquals(IllegalArgumentException.Argument.USER_NAME,exception.getArgument());
        Assertions.assertEquals(IllegalArgumentException.Validation.NOT_EMPTY,exception.getValidationFailed());
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    public void createNewCredentialWhenEmptyWebSite(String webSite){

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,()->target.createNewWebCredential(faker.internet().password(), faker.name().username(), webSite,UUID.randomUUID()));
        Assertions.assertEquals(IllegalArgumentException.Argument.WEB_SITE,exception.getArgument());
        Assertions.assertEquals(IllegalArgumentException.Validation.NOT_EMPTY,exception.getValidationFailed());
    }

    @Test
    public void findCredentialWhenOk(){
        UUID userId = UUID.randomUUID();
        WebCredential newCredential = WebCredential
                .builder()
                .password(faker.internet().password())
                .userName(faker.name().username())
                .credentialName(faker.internet().domainName())
                .id(UUID.randomUUID())
                .build();
        Mockito.doReturn(newCredential).when(fetcher).findById(newCredential.getId(),userId);
        WebCredential credentialFound = target.findById(newCredential.getId(),userId);
        Assertions.assertEquals(newCredential,credentialFound);
    }

    @Test
    public void findCredentialByUserIdWhenOk(){
        PagingContext paging = PagingContext.builder().pageNumber(1).pageSize(5).build();
        UUID userId = UUID.randomUUID();
        List<WebCredential> newCredential = WebCredentialMother.multipleDummyRandomFullCredential(5,userId);
        Mockito.doReturn(newCredential).when(fetcher).findByUserId(userId,paging);
        List<WebCredential> credentialFound = target.findByUserId(userId,paging);
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
