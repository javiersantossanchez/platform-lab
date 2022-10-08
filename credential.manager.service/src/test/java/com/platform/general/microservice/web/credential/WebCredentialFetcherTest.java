package com.platform.general.microservice.web.credential;

import com.github.javafaker.Faker;
import com.platform.general.microservice.web.credential.exceptions.InvalidPasswordException;
import com.platform.general.microservice.web.credential.ports.out.WebCredentialRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class WebCredentialFetcherTest {

    private final Faker faker = new Faker();

    @InjectMocks
    WebCredentialFetcher target;

    @Mock
    WebCredentialRepository repository;

    @Test()
    public void fetchAllWhenOK(){
        List<WebCredential> expected = new ArrayList<>();
        Mockito.doReturn(expected).when(repository).findAll();
        List<WebCredential> result = target.fetchAll();
        Mockito.verify(repository,Mockito.times(1)).findAll();
        Assertions.assertEquals(expected,result);
    }

}
