package com.platform.general.microservice.web.credential.adapters.postgresql;

import com.github.javafaker.Faker;
import com.platform.general.microservice.web.credential.exceptions.WebCredentialDeleteException;
import com.platform.general.microservice.web.credential.exceptions.WebCredentialNotFoundException;
import com.platform.general.microservice.web.credential.exceptions.WebCredentialRegistrationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.UUID;

@Testcontainers
@SpringBootTest
@ImportAutoConfiguration(exclude = {EmbeddedMongoAutoConfiguration.class})
public class WebCredentialPostgresqlRepositoryComponentTest {

    private final Faker faker = new Faker();

    @Autowired
    private WebCredentialPostgresqlRepository target;

    @MockBean
    private WebCredentialDao repo;

    @MockBean
    @SuppressWarnings("unused")
    private JwtDecoder jwtDecoder;

    @Container
    static PostgreSQLContainer postgreSQLDBContainer = new PostgreSQLContainer("postgres:14.5");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLDBContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLDBContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLDBContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", postgreSQLDBContainer::getDriverClassName);
    }


    @Test()
    public void createOneCredentialWithRetryOnError(){
        String userName = faker.name().username();
        String credentialName = faker.internet().domainName();
        String password = faker.internet().password();
        LocalDateTime now = LocalDateTime.now();
        UUID userId = UUID.randomUUID();
        WebCredentialEntity entity = WebCredentialEntity.builder()
                .password(password)
                .userName(userName)
                .credentialName(credentialName)
                .creationTime(now)
                .userId(userId)
                .build();
        Mockito.doThrow(RuntimeException.class).when(repo).save(entity);

        Assertions.assertThrows(WebCredentialRegistrationException.class,()->target.save(password, userName, credentialName,now,userId));

        Mockito.verify(repo,Mockito.times(3)).save(entity);
    }

    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////
    @Test()
    public void deleteOneCredentialWithRetryOnDatabaseError(){
        UUID id = UUID.randomUUID();
        Mockito.doThrow(RuntimeException.class).when(repo).deleteById(id);

        Assertions.assertThrows(WebCredentialDeleteException.class,()->target.deleteById(id));

        Mockito.verify(repo,Mockito.times(3)).deleteById(id);
    }

    @Test()
    public void deleteOneCredentialWithNotRetryOnNotFoundError(){
        UUID id = UUID.randomUUID();
        Mockito.doThrow(EmptyResultDataAccessException.class).when(repo).deleteById(id);

        Assertions.assertThrows(WebCredentialNotFoundException.class,()->target.deleteById(id));

        Mockito.verify(repo,Mockito.times(1)).deleteById(id);
    }
}
