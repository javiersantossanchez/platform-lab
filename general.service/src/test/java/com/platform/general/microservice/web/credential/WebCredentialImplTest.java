package com.platform.general.microservice.web.credential;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class WebCredentialImplTest {

    private final Faker faker = new Faker();

    @InjectMocks
    WebCredentialImpl target;

    @Mock
    WebCredentialCreator creator;

    @Mock
    WebCredentialFetcher fetcher;

    @Mock
    WebCredentialDeleter deleter;

    @Test
    public void createNewCredentialWhenOK(){

        WebCredential newCredential = new WebCredential();
        newCredential.setPassword(faker.internet().password());
        newCredential.setUserName(faker.name().username());
        newCredential.setWebSite(faker.internet().domainName());
        newCredential.setId(UUID.randomUUID());
        Mockito.doReturn(newCredential).when(creator).create(newCredential.getPassword(),newCredential.getUserName(),newCredential.getWebSite());

        WebCredential credentialCreated = target.createNewWebCredential(newCredential.getPassword(), newCredential.getUserName(), newCredential.getWebSite());
        Assertions.assertEquals(newCredential.getPassword(),credentialCreated.getPassword());
        Assertions.assertEquals(newCredential.getUserName(),credentialCreated.getUserName());
        Assertions.assertEquals(newCredential.getWebSite(),credentialCreated.getWebSite());
        Assertions.assertNotNull(credentialCreated.getId());

        Mockito.verify(creator,Mockito.times(1)).create(newCredential.getPassword(), newCredential.getUserName(), newCredential.getWebSite());
    }

    @Test
    public void findCredentialWhenOk(){
        WebCredential newCredential = new WebCredential();
        newCredential.setPassword(faker.internet().password());
        newCredential.setUserName(faker.name().username());
        newCredential.setWebSite(faker.internet().domainName());
        newCredential.setId(UUID.randomUUID());
        Mockito.doReturn(newCredential).when(fetcher).findById(newCredential.getId());
        WebCredential credentialFound = target.findCredential(newCredential.getId());
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
