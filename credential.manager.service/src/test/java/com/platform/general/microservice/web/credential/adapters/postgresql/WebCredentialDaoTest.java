package com.platform.general.microservice.web.credential.adapters.postgresql;

import com.github.javafaker.Faker;
import com.platform.general.microservice.web.credential.exceptions.InvalidArgumentException;
import com.platform.general.microservice.web.credential.test.utils.WebCredentialEntityMother;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.platform.general.microservice.web.credential.exceptions.InvalidArgumentException.Error.PASSWORD_SIZE_BIGGER_THAN_VALUE_ALLOWS;
import static org.postgresql.util.PSQLState.NOT_NULL_VIOLATION;


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


////////////////////////////////////////////////////////////////
/////////////TEST CREATE NEW CREDENTIAL/////////////////////////
////////////////////////////////////////////////////////////////
    @Test
    public void createOneCredentialWhenOk(){

        String username = faker.name().username();
        String credentialName = faker.internet().domainName();
        String password = faker.internet().password();
        LocalDateTime creationDate = LocalDateTime.now();

        WebCredentialEntity entity = WebCredentialEntity.builder()
                        .password(password)
                        .userName(username)
                        .credentialName(credentialName)
                        .creationTime(creationDate)
                        .userId(UUID.randomUUID())
                        .build();
        WebCredentialEntity result = repository.save(entity);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(entity,result);
    }

    @Test
    public void createOneCredentialWithNullAsPassword(){

        String username = faker.name().username();
        String credentialName = faker.internet().domainName();
        String password = null;

        WebCredentialEntity entity = WebCredentialEntity.builder()
                .password(password)
                .userName(username)
                .credentialName(credentialName)
                .creationTime(LocalDateTime.now())
                .userId(UUID.randomUUID())
                .build();
        Assertions.assertThrows(DataIntegrityViolationException.class,()-> repository.save(entity));
    }

    @Test
    public void createOneCredentialWithNullAsCredentialName(){

        String username = faker.name().username();
        String credentialName = null;
        String password = faker.internet().password();

        WebCredentialEntity entity = WebCredentialEntity.builder()
                .password(password)
                .userName(username)
                .credentialName(credentialName)
                .creationTime(LocalDateTime.now())
                .userId(UUID.randomUUID())
                .build();
        Assertions.assertThrows(DataIntegrityViolationException.class,()-> repository.save(entity));
    }

    @Test
    public void createOneCredentialWithNullAsUserName(){

        String username = null;
        String credentialName = faker.internet().domainName();
        String password = faker.internet().password();

        WebCredentialEntity entity = WebCredentialEntity.builder()
                .password(password)
                .userName(username)
                .credentialName(credentialName)
                .creationTime(LocalDateTime.now())
                .userId(UUID.randomUUID())
                .build();
        Assertions.assertThrows(DataIntegrityViolationException.class,()-> repository.save(entity));
    }

    @Test
    public void createOneCredentialWithNullAsUserId(){

        WebCredentialEntity entity = WebCredentialEntity.builder()
                .password(faker.internet().password())
                .userName(faker.name().username())
                .credentialName(faker.internet().domainName())
                .creationTime(LocalDateTime.now())
                .userId(null)
                .build();
        Assertions.assertThrows(DataIntegrityViolationException.class,()->repository.save(entity));
    }

    @Test
    public void createOneCredentialWithPasswordBiggerAsRequired(){

        String username = faker.name().username();
        String credentialName = faker.internet().domainName();
        String password = faker.internet().password(51,60);

        WebCredentialEntity entity = WebCredentialEntity.builder()
                .password(password)
                .userName(username)
                .credentialName(credentialName)
                .creationTime(LocalDateTime.now())
                .userId(UUID.randomUUID())
                .build();
        Assertions.assertThrows(DataIntegrityViolationException.class,()-> repository.save(entity));
    }

    @Test
    public void createOneCredentialWithCredentialNameBiggerAsRequired(){

        String username = faker.name().username();
        String credentialName = faker.lorem().characters(101,110);
        String password = faker.internet().password();

        WebCredentialEntity entity = WebCredentialEntity.builder()
                .password(password)
                .userName(username)
                .credentialName(credentialName)
                .creationTime(LocalDateTime.now())
                .userId(UUID.randomUUID())
                .build();
        Assertions.assertThrows(DataIntegrityViolationException.class,()-> repository.save(entity));
    }

    @Test
    public void createOneCredentialWithUserNameBiggerAsRequired(){

        String username = faker.lorem().characters(101,110);
        String credentialName = faker.internet().domainName();
        String password = faker.internet().password();

        WebCredentialEntity entity = WebCredentialEntity.builder()
                .password(password)
                .userName(username)
                .credentialName(credentialName)
                .creationTime(LocalDateTime.now())
                .userId(UUID.randomUUID())
                .build();
        Assertions.assertThrows(DataIntegrityViolationException.class,()-> repository.save(entity));
    }

    @Test
    public void createOneCredentialWithUserNameRepeated(){
        String username = faker.name().username();

        WebCredentialEntity entity = WebCredentialEntity.builder()
                .password(faker.internet().password())
                .userName(username)
                .credentialName(faker.internet().domainName())
                .creationTime(LocalDateTime.now())
                .userId(UUID.randomUUID())
                .build();

        repository.save(entity);
        final WebCredentialEntity duplicatedEntity =  WebCredentialEntity.builder()
                .password(faker.internet().password())
                .userName(username)
                .credentialName(faker.internet().domainName())
                .creationTime(LocalDateTime.now())
                .build();
        Assertions.assertThrows(DataIntegrityViolationException.class,()->repository.save(duplicatedEntity));
    }



////////////////////////////////////////////////////////////////
/////////////TEST FIND CREDENTIAL BY ID/////////////////////////
////////////////////////////////////////////////////////////////
    @Test
    public void findCredentialByIDWithNull(){
        Assertions.assertThrows(InvalidDataAccessApiUsageException.class,()-> repository.findById(null));
    }

    @Test
    public void findCredentialByIDAndUserIDWhenCredentialBelongsToOtherUser(){
        WebCredentialEntity entityCreated = repository.save(WebCredentialEntityMother.DummyRandomCredential());
        UUID idOfDifferentUser;
        do{
            idOfDifferentUser = UUID.randomUUID();
        }while(idOfDifferentUser.equals(entityCreated.getUserId()));
        Optional<WebCredentialEntity> entityFound = repository.findOneByIdAndUserId(entityCreated.getId(),idOfDifferentUser);
        Assertions.assertFalse(entityFound.isPresent());
    }

    @Test
    public void findCredentialByIDAndUserIDWhenOk(){
        WebCredentialEntity entityCreated = repository.save(WebCredentialEntityMother.DummyRandomCredential());
        Optional<WebCredentialEntity> entityFound = repository.findOneByIdAndUserId(entityCreated.getId(),entityCreated.getUserId());
        Assertions.assertTrue(entityFound.isPresent());
        Assertions.assertEquals(entityCreated,entityFound.get());
    }

    @Test
    public void findCredentialByIDAndUserIDWithParamsAsNull(){
        Optional<WebCredentialEntity> entityFound = repository.findOneByIdAndUserId(null,null);
        Assertions.assertTrue(entityFound.isEmpty());
    }



////////////////////////////////////////////////////////////////
////////////////TEST DELETE CREDENTIAL//////////////////////////
////////////////////////////////////////////////////////////////
    @Test
    public void deleteCredentialWhenIdDoesNotExist(){
        Assertions.assertThrows(EmptyResultDataAccessException.class,()-> repository.deleteById(UUID.randomUUID()));
    }



////////////////////////////////////////////////////////////////
///////////////TEST FIND ALL CREDENTIALS/////////////////////////
////////////////////////////////////////////////////////////////
    @Test
    public void findAllCredentialByIDAndUserIDWhenOk(){
        UUID userId1 = UUID.randomUUID();
        UUID userId2 = UUID.randomUUID();
        List<WebCredentialEntity> credentialListByUser1 = WebCredentialEntityMother.multipleDummyRandomCredential(5,userId1);
        List<WebCredentialEntity> credentialListByUser2 = WebCredentialEntityMother.multipleDummyRandomCredential(1,userId2);

        credentialListByUser1.parallelStream().forEach(currentCredential -> repository.save(currentCredential));
        credentialListByUser2.parallelStream().forEach(currentCredential -> repository.save(currentCredential));

        Pageable sortedByName =
                PageRequest.of(0, 10, Sort.by("credentialName"));
        List<WebCredentialEntity> entitiesFound = repository.findByUserId(userId1,sortedByName);
        Assertions.assertNotNull(entitiesFound);
        Assertions.assertFalse(entitiesFound.isEmpty());
        Assertions.assertEquals(credentialListByUser1.size(),entitiesFound.size());
    }


    @Test
    public void findAllCredentialByIDAndUserIDWhenThereAreNotCredentials(){
        UUID userId1 = UUID.randomUUID();
        UUID userId2 = UUID.randomUUID();
        List<WebCredentialEntity> credentialListByUser1 = WebCredentialEntityMother.multipleDummyRandomCredential(1,userId1);
        credentialListByUser1.parallelStream().forEach(currentCredential -> repository.save(currentCredential));

        Pageable sortedByName =
                PageRequest.of(0, 10, Sort.by("credentialName"));
        List<WebCredentialEntity> entitiesFound = repository.findByUserId(userId2,sortedByName);
        Assertions.assertNotNull(entitiesFound);
        Assertions.assertTrue(entitiesFound.isEmpty());
    }

    @Test
    public void findAllCredentialByIDAndUserIDWhenUserIdIsNull(){

        Pageable sortedByName =
                PageRequest.of(0, 10, Sort.by("credentialName"));
        List<WebCredentialEntity> entitiesFound = repository.findByUserId(null,sortedByName);
        Assertions.assertNotNull(entitiesFound);
        Assertions.assertTrue(entitiesFound.isEmpty());
    }


////////////////////////////////////////////////////////////////
//////////////////TEST UPDATE PASSWORD /////////////////////////
////////////////////////////////////////////////////////////////

    @Test
    public void updatePasswordCredentialWhenOK(){

        final String expectedPassword = "new-value-for-password";
        final String originalPassword ="original-value";
        WebCredentialEntity credential = repository.save(WebCredentialEntityMother.DummyRandomCredentialWithPassword(originalPassword));

        int recordsUpdated = repository.updatePassword(expectedPassword,credential.getId());
        credential =repository.findById(credential.getId()).orElse(null);

        Assertions.assertEquals(1,recordsUpdated);
        Assertions.assertNotNull(credential);
        Assertions.assertEquals(expectedPassword,credential.getPassword());
    }

    @Test
    public void updatePasswordCredentialWhenCredentialDoesNotExist(){

        final String expectedPassword = "new-value-for-password";
        final UUID credentialId = UUID.randomUUID();

        int recordsUpdated = repository.updatePassword(expectedPassword,credentialId);
        WebCredentialEntity credential =repository.findById(credentialId).orElse(null);

        Assertions.assertEquals(0,recordsUpdated);
        Assertions.assertNull(credential);
    }

    @Test
    public void updatePasswordCredentialWithPasswordBiggerAsRequired(){
        final String sqlErrorCodeExpected = "22001";
        String sqlErrorCodeActual=null;
        final String errorMessageExpected ="ERROR: value too long for type character varying(50)";
        String errorMessageActual=null;
        final String expectedPassword = faker.internet().password(51,60);
        WebCredentialEntity credential = repository.save(WebCredentialEntityMother.DummyRandomCredential());
        DataIntegrityViolationException exception = Assertions.assertThrows(DataIntegrityViolationException.class,()-> repository.updatePassword(expectedPassword,credential.getId()));
        if(exception.getCause() instanceof DataException){
            DataException dataException = (DataException) exception.getCause();
            if(dataException.getCause() instanceof PSQLException) {
                PSQLException psqlException = (PSQLException) dataException.getCause();
                sqlErrorCodeActual = psqlException.getSQLState();
                errorMessageActual = psqlException.getMessage();
            }
        }
        Assertions.assertEquals(sqlErrorCodeExpected,sqlErrorCodeActual);
        Assertions.assertEquals(errorMessageExpected,errorMessageActual);
    }

    @Test
    public void updatePasswordCredentialWithNullAsPassword(){
        final String expectedPassword = null;
        final String sqlErrorCodeExpected = NOT_NULL_VIOLATION.getState();
        String sqlErrorCodeActual=null;
        final String errorMessageExpected ="ERROR: null value in column \"password\" of relation \"user_password_credential\" violates not-null constraint";
        String errorMessageActual=null;
        WebCredentialEntity credential = repository.save(WebCredentialEntityMother.DummyRandomCredential());
        DataIntegrityViolationException exception =Assertions.assertThrows(DataIntegrityViolationException.class,()-> repository.updatePassword(expectedPassword,credential.getId()));
        if(exception.getCause() instanceof ConstraintViolationException){
            ConstraintViolationException dataException = (ConstraintViolationException) exception.getCause();
            if(dataException.getCause() instanceof PSQLException) {
                PSQLException psqlException = (PSQLException) dataException.getCause();
                sqlErrorCodeActual = psqlException.getSQLState();
                errorMessageActual = psqlException.getMessage();
            }
        }
        Assertions.assertEquals(sqlErrorCodeExpected,sqlErrorCodeActual);
        Assertions.assertTrue(errorMessageActual.startsWith(errorMessageExpected));
    }


}
