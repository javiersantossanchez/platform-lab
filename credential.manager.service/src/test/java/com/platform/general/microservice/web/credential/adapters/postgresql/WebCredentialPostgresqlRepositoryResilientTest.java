package com.platform.general.microservice.web.credential.adapters.postgresql;

import com.github.javafaker.Faker;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.CannotCreateTransactionException;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

@Testcontainers
@SpringBootTest
@ImportAutoConfiguration(exclude = {EmbeddedMongoAutoConfiguration.class})
public class WebCredentialPostgresqlRepositoryResilientTest {

    private final Faker faker = new Faker();

    @Autowired
    private WebCredentialPostgresqlRepositoryResilient target;


    @MockBean
    private WebCredentialDao repo;

    @Autowired
    // Real CircuitBreakerRegistry is wired in to be able to set
    private CircuitBreakerRegistry circuitBreakerRegistry;

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


    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////
    @Test
    public void findCredentialByCredentialIdAndUserIdWithRetryOnDatabaseError(){
        circuitBreakerRegistry.circuitBreaker("CircuitBreakerWebCredentialPostgresqlRepositoryResilient")
                .transitionToClosedState();
        UUID credentialId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Mockito.doThrow(RuntimeException.class).when(repo).findOneByIdAndUserId(credentialId,userId);
        Assertions.assertThrows(RuntimeException.class,()->target.find(credentialId,userId));
        Mockito.verify(repo,Mockito.times(3)).findOneByIdAndUserId(credentialId,userId);
    }

    @Test
    public void findCredentialByCredentialIdAndUserIdWithCircuitBreakerOnDatabaseError(){
        circuitBreakerRegistry.circuitBreaker("CircuitBreakerWebCredentialPostgresqlRepositoryResilient")
                .transitionToClosedState();
        UUID credentialId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Mockito.doThrow(new CannotCreateTransactionException("Mock Error")).when(repo).findOneByIdAndUserId(credentialId,userId);

        Assertions.assertThrows(CannotCreateTransactionException.class,()->target.find(credentialId,userId));
        Assertions.assertThrows(CallNotPermittedException.class,()->target.find(credentialId,userId));
        Assertions.assertThrows(CallNotPermittedException.class,()->target.find(credentialId,userId));
        Assertions.assertThrows(CannotCreateTransactionException.class,()->target.find(credentialId,userId));
        Mockito.verify(repo,Mockito.times(6)).findOneByIdAndUserId(credentialId,userId);
    }
    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////
    @Test
    public void findCredentialByUserIdWithCircuitBreakerOnDatabaseError(){
        circuitBreakerRegistry.circuitBreaker("CircuitBreakerWebCredentialPostgresqlRepositoryResilient")
                .transitionToClosedState();
        UUID userId = UUID.randomUUID();
        Pageable sortedByName =
                PageRequest.of(0, 10, Sort.by("credentialName"));
        Mockito.doThrow(new CannotCreateTransactionException("Mock Error")).when(repo).findByUserId(userId,sortedByName);
        Assertions.assertThrows(RuntimeException.class,()->target.findByUserId(userId,sortedByName));
        Mockito.verify(repo,Mockito.times(3)).findByUserId(userId,sortedByName);
    }

    @Test
    public void findCredentialByUserIdWithRetryOnDatabaseError(){
        circuitBreakerRegistry.circuitBreaker("CircuitBreakerWebCredentialPostgresqlRepositoryResilient")
                .transitionToClosedState();
        UUID userId = UUID.randomUUID();
        Pageable sortedByName =
                PageRequest.of(0, 10, Sort.by("credentialName"));
        Mockito.doThrow(new CannotCreateTransactionException("Mock Error")).when(repo).findByUserId(userId,sortedByName);
        Assertions.assertThrows(CannotCreateTransactionException.class,()->target.findByUserId(userId,sortedByName));
        Assertions.assertThrows(CallNotPermittedException.class,()->target.findByUserId(userId,sortedByName));
        Assertions.assertThrows(CallNotPermittedException.class,()->target.findByUserId(userId,sortedByName));
        Mockito.verify(repo,Mockito.times(5)).findByUserId(userId,sortedByName);
    }

}
