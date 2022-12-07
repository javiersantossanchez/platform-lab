package com.platform.general.microservice.web.credential.test.utils;

import com.github.javafaker.Faker;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.UUID;

public class JwtMother {

    private static final Faker faker = new Faker();


    public static Jwt DummyRandomJwt(UUID userId) {
        return  DummyRandomJwt(userId.toString());
    }

    public static Jwt InvalidRandomJwt() {
        return DummyRandomJwt("invalid-user-id");
    }

    private static Jwt DummyRandomJwt(String userId) {
        return  Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("sub", userId)
                .claim("scope", "openid profile email")
                .claim("sid", UUID.randomUUID())
                .claim("given_name", faker.name().firstName())
                .claim("family_name", faker.name().lastName())
                .build();
    }

}
