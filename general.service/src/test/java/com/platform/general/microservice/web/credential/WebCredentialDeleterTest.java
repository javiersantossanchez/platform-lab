package com.platform.general.microservice.web.credential;

import com.github.javafaker.Faker;
import com.platform.general.microservice.web.credential.ports.out.WebCredentialRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class WebCredentialDeleterTest {

    private final Faker faker = new Faker();

    @InjectMocks
    WebCredentialDeleter target;

    @Mock
    WebCredentialRepository repository;

    @Test()
    public void fetchAllWhenOK(){
        UUID idToDelete = UUID.randomUUID();
        Mockito.doNothing().when(repository).deleteById(idToDelete);
        target.deleteById(idToDelete);
        Mockito.verify(repository,Mockito.times(1)).deleteById(idToDelete);
    }

}
