package com.platform.general.microservice.credential;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PasswordAuthenticationCredentialCreatorDummyTest {

    private PasswordAuthenticationCredentialCreatorDummy target;

    private final Faker faker = new Faker();

    //@Test
    public void asdsa(){
        String username = faker.name().username();
        String password = faker.internet().password();
        target = new PasswordAuthenticationCredentialCreatorDummy();
        PasswordAuthenticationCredential result = target.create(password,username);
        System.out.println(username);
        System.out.println(password);
        System.out.println(result.toString());
        Assertions.assertEquals(result.getPassword(),password);
        Assertions.assertEquals(result.getUserName(),username);
    }
}
