package com.platform.general.microservice.web.credential;

import com.github.javafaker.Faker;
import com.platform.general.microservice.web.credential.exceptions.*;
import com.platform.general.microservice.web.credential.exceptions.IllegalArgumentException;
import com.platform.general.microservice.web.credential.ports.out.WebCredentialRepository;
import com.platform.general.microservice.web.credential.utils.PagingContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class WebCredentialFetcherTest {

    private final Faker faker = new Faker();

    @InjectMocks
    WebCredentialFetcherImpl target;

    @Mock
    WebCredentialRepository repository;

    @Test
    public void findCredentialWhenOK(){
        UUID credentialId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Mockito.doReturn(WebCredential.builder().build()).when(repository).findById(credentialId,userId);
        target.findById(credentialId,userId);
        Mockito.verify(repository,Mockito.times(1)).findById(credentialId,userId);
    }

    @Test
    public void findCredentialWhenAnErrorGeneratedOnSearch(){
        UUID credentialId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Mockito.doThrow(new WebCredentialSearchException()).when(repository).findById(credentialId,userId);
        Assertions.assertThrows(WebCredentialSearchException.class,()-> target.findById(credentialId,userId));
    }

    @Test
    public void findCredentialWhenCredentialDoesNotExist(){
        UUID credentialId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Mockito.doThrow(new WebCredentialNotFoundException()).when(repository).findById(credentialId,userId);
        Assertions.assertThrows(WebCredentialNotFoundException.class,()-> target.findById(credentialId,userId));
    }

    @Test
    public void findCredentialWithNullAsUserId(){
        Assertions.assertThrows(EmptyUserIdException.class,()->target.findById(UUID.randomUUID(),null));
    }

    @Test()
    public void findCredentialWithNullAsCredentialId(){
        Assertions.assertThrows(IllegalArgumentException.class,()->target.findById(null,UUID.randomUUID()));
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////

    @Test
    public void findCredentialByUserWithUserIdAsNull(){
        Assertions.assertThrows(InvalidUserInformationException.class,()->target.findByUserId(null,null));
    }

    @Test
    public void findCredentialByUserWhenAnErrorGeneratedOnSearch(){
        UUID userId = UUID.randomUUID();
        PagingContext paging = PagingContext.builder().pageNumber(1).pageSize(5).build();
        Mockito.doThrow(new WebCredentialSearchException()).when(repository).findById(userId,paging);
        Assertions.assertThrows(WebCredentialSearchException.class,()-> target.findByUserId(userId,paging));
    }

}
