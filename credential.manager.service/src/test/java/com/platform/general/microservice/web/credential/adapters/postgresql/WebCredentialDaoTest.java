package com.platform.general.microservice.web.credential.adapters.postgresql;

import com.github.javafaker.Faker;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
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

    @MockBean
    @SuppressWarnings("unused")
    private JwtDecoder jwtDecoder;


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
    public void createOneCredentialWithUserNameRepeated(){
        String username = faker.name().username();

        WebCredentialEntity entity = new WebCredentialEntity(faker.internet().password(),username,faker.internet().domainName(),LocalDateTime.now());
        repository.save(entity);
        final WebCredentialEntity duplicatedEntity = new WebCredentialEntity(faker.internet().password(),username,faker.internet().domainName(),LocalDateTime.now());
        Assertions.assertThrows(DataIntegrityViolationException.class,()->repository.save(duplicatedEntity));
    }
    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////


    @Test
    public void findCredentialByIDWithNull(){
        Assertions.assertThrows(InvalidDataAccessApiUsageException.class,()->{repository.findById(null);});
    }

    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////

    @Test
    public void deleteCredentialWhenIdDoesNotExist(){
        Assertions.assertThrows(EmptyResultDataAccessException.class,()->{repository.deleteById(UUID.randomUUID());});
    }


}
