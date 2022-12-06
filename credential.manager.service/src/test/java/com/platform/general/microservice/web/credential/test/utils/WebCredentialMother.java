package com.platform.general.microservice.web.credential.test.utils;

import com.github.javafaker.Faker;
import com.platform.general.microservice.web.credential.WebCredential;
import com.platform.general.microservice.web.credential.adapters.postgresql.WebCredentialEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WebCredentialMother {

    private static final Faker faker = new Faker();


    public static WebCredential DummyRandomFullCredential() {
        return  WebCredentialMother.DummyRandomCredential(UUID.randomUUID());
    }

    public static List<WebCredential> multipleDummyRandomFullCredential(int listSize, UUID userId){
        List<WebCredential> credentialLst = new ArrayList<>(listSize);
        for(int index = 0; index < listSize;index ++){
            credentialLst.add(WebCredentialMother.DummyRandomCredential(userId));
        }
        return  credentialLst;
    }

    private static WebCredential DummyRandomCredential(UUID userId) {
        return  WebCredential
                .builder()
                .password(faker.internet().password())
                .userName(faker.name().username())
                .credentialName(faker.internet().domainName())
                .id(UUID.randomUUID())
                .userId(userId)
                .build();
    }


}
