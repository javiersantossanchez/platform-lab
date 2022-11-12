package com.platform.general.microservice.web.credential.adapters.postgresql;

import com.github.javafaker.Faker;
import com.platform.general.microservice.web.credential.AuditEvent;
import com.platform.general.microservice.web.credential.exceptions.AuditEventRegistrationException;
import com.platform.general.microservice.web.credential.exceptions.WebCredentialRegistrationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.UUID;


@Testcontainers
@SpringBootTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ImportAutoConfiguration(exclude = {EmbeddedMongoAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class})
@ActiveProfiles(value = "test")
public class WebCredentialDaoTest  {

    private final Faker faker = new Faker();

    @Container
    static PostgreSQLContainer postgreSQLDBContainer = new PostgreSQLContainer("postgres:14.5");

    @Autowired
    private WebCredentialDao repository;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLDBContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLDBContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLDBContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", postgreSQLDBContainer::getDriverClassName);
    }


    @Test
    public void createOneCredentialWhenOk(){

        String username = faker.name().username();
        String credentialName = faker.internet().domainName();
        String password = faker.internet().password();
        LocalDateTime creationDate = LocalDateTime.now();

        WebCredentialEntity entity = new WebCredentialEntity(password,username,credentialName,creationDate);
        repository.save(entity);
        WebCredentialEntity result = repository.findById(entity.getId()).orElse(null);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(entity,result);
    }

    @Test
    public void createOneCredentialWithNullAsPassword(){

        String username = faker.name().username();
        String credentialName = faker.internet().domainName();
        String password = null;

        WebCredentialEntity entity = new WebCredentialEntity(password,username,credentialName);
        Assertions.assertThrows(DataIntegrityViolationException.class,()->{repository.save(entity);});
    }

    @Test
    public void createOneCredentialWithNullAsCredentialName(){

        String username = faker.name().username();
        String credentialName = null;
        String password = faker.internet().password();

        WebCredentialEntity entity = new WebCredentialEntity(password,username,credentialName);
        Assertions.assertThrows(DataIntegrityViolationException.class,()->{repository.save(entity);});
    }

    @Test
    public void createOneCredentialWithNullAsUserName(){

        String username = null;
        String credentialName = faker.internet().domainName();
        String password = faker.internet().password();

        WebCredentialEntity entity = new WebCredentialEntity(password,username,credentialName);
        Assertions.assertThrows(DataIntegrityViolationException.class,()->{repository.save(entity);});
    }

    @Test
    public void createOneCredentialWithPasswordBiggerAsRequired(){

        String username = faker.name().username();
        String credentialName = faker.internet().domainName();
        String password = faker.internet().password(51,60);

        WebCredentialEntity entity = new WebCredentialEntity(password,username,credentialName);
        Assertions.assertThrows(DataIntegrityViolationException.class,()->{repository.save(entity);});
    }

    @Test
    public void createOneCredentialWithCredentialNameBiggerAsRequired(){

        String username = faker.name().username();
        String credentialName = faker.lorem().characters(101,110);
        String password = faker.internet().password();

        WebCredentialEntity entity = new WebCredentialEntity(password,username,credentialName);
        Assertions.assertThrows(DataIntegrityViolationException.class,()->{repository.save(entity);});
    }

    @Test
    public void createOneCredentialWithUserNameBiggerAsRequired(){

        String username = faker.lorem().characters(101,110);
        String credentialName = faker.internet().domainName();
        String password = faker.internet().password();

        WebCredentialEntity entity = new WebCredentialEntity(password,username,credentialName);
        Assertions.assertThrows(DataIntegrityViolationException.class,()->{repository.save(entity);});
    }



    @Test
    public void findCredentialByIDWithNull(){
        Assertions.assertThrows(InvalidDataAccessApiUsageException.class,()->{repository.findById(null);});
    }

    @Test
    public void deleteCredentialWhenIdDoesNotExist(){
        Assertions.assertThrows(EmptyResultDataAccessException.class,()->{repository.deleteById(UUID.randomUUID());});
    }


}
