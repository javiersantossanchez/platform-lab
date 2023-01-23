package com.platform.general.microservice.web.credential.test.utils;

import com.github.javafaker.Faker;
import com.platform.general.microservice.web.credential.adapters.postgresql.WebCredentialEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WebCredentialEntityMother {

    private static final Faker faker = new Faker();

        /**
         * Create a full dummy credential (Without id), The data is random generated every time is call.
         * @return a full dummy credential
         */
    public static WebCredentialEntity DummyRandomCredential() {
        return  WebCredentialEntityMother.DummyRandomCredential(UUID.randomUUID());
    }

    public static WebCredentialEntity DummyRandomCredentialWithPassword(String password) {
        return  WebCredentialEntityMother.DummyRandomCredential(UUID.randomUUID(),password);
    }

    private static WebCredentialEntity DummyRandomCredential(UUID userId) {
        return DummyRandomCredential(userId,faker.internet().password());
    }

    private static WebCredentialEntity DummyRandomCredential(UUID userId,String password) {
        return WebCredentialEntity.builder()
                .password(password)
                .userName(faker.name().username())
                .credentialName(faker.internet().domainName())
                .creationTime(LocalDateTime.now())
                .userId(userId)
                .build();
    }

    public static List<WebCredentialEntity> multipleDummyRandomCredential(int listSize,UUID userId){
        List<WebCredentialEntity> credentialLst = new ArrayList<>(listSize);
        for(int index = 0; index < listSize;index ++){
            credentialLst.add(WebCredentialEntityMother.DummyRandomCredential(userId));
        }
        return  credentialLst;
    }
}
