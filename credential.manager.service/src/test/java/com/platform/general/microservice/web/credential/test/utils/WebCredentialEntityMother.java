package com.platform.general.microservice.web.credential.test.utils;

import com.github.javafaker.Faker;
import com.platform.general.microservice.web.credential.adapters.postgresql.WebCredentialEntity;

import java.time.LocalDateTime;
import java.util.UUID;

public class WebCredentialEntityMother {

    private static final Faker faker = new Faker();

        /**
         * Create a full dummy credential (Without id), The data is random generated every time is call.
         * @return a full dummy credential
         */
    public static WebCredentialEntity DummyRandomCredential() {
        return WebCredentialEntity.builder()
                .password(faker.internet().password())
                .userName(faker.name().username())
                .credentialName(faker.internet().domainName())
                .creationTime(LocalDateTime.now())
                .userId(UUID.randomUUID())
                .build();
    }
}
