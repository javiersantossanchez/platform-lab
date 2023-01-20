package com.platform.general.microservice.web.credential.worker;

import com.github.javafaker.Faker;
import com.platform.general.microservice.web.credential.WebCredential;
import com.platform.general.microservice.web.credential.exceptions.*;
import com.platform.general.microservice.web.credential.ports.out.WebCredentialRepository;
import com.platform.general.microservice.web.credential.utils.PagingContext;
import com.platform.general.microservice.web.credential.worker.WebCredentialFetcherImpl;
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
        Mockito.doThrow(new WebCredentialGeneralException()).when(repository).findById(credentialId,userId);
        Assertions.assertThrows(WebCredentialGeneralException.class,()-> target.findById(credentialId,userId));
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
        Assertions.assertThrows(InvalidArgumentException.class,()->target.findById(UUID.randomUUID(),null));
    }

    @Test()
    public void findCredentialWithNullAsCredentialId(){
        Assertions.assertThrows(InvalidArgumentException.class,()->target.findById(null,UUID.randomUUID()));
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////

    @Test
    public void findCredentialByUserWithUserIdAsNull(){
        Assertions.assertThrows(InvalidArgumentException.class,()->target.findByUserId(null,null));
    }

    @Test
    public void findCredentialByUserWhenAnErrorGeneratedOnSearch(){
        UUID userId = UUID.randomUUID();
        PagingContext paging = PagingContext.builder().pageNumber(1).pageSize(5).build();
        Mockito.doThrow(new WebCredentialGeneralException()).when(repository).findById(userId,paging);
        Assertions.assertThrows(WebCredentialGeneralException.class,()-> target.findByUserId(userId,paging));
    }

}
