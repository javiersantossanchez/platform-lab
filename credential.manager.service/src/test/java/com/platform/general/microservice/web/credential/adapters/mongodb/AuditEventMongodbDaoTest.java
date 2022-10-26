package com.platform.general.microservice.web.credential.adapters.mongodb;

import com.github.javafaker.Faker;
import com.platform.general.microservice.web.credential.AuditEvent;
import com.platform.general.microservice.web.credential.utils.DateManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
@ComponentScan({"com.platform.general.microservice.web.credential.utils"})
public class AuditEventMongodbDaoTest {

    private final Faker faker = new Faker();

    @Autowired
    private AuditEventMongodbDao repository;

    @Autowired
    private DateManager dateManager;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0.2");

    @Test
    void saveWhenOK(){
        Events eventExpected = Events.builder()
                .eventDate(dateManager.getCurrentLocalDate())
                .eventType(AuditEvent.AuditEventType.SEARCH_WEB_CREDENTIAL  )
                .id(faker.number().toString())
                .build();
        repository.save(eventExpected);

       Events eventCreated = repository.findById(eventExpected.getId()).get();
        repository.deleteById(eventExpected.getId());

        Assertions.assertEquals(eventExpected.getEventDate().getDayOfMonth(),eventCreated.getEventDate().getDayOfMonth());
        Assertions.assertEquals(eventExpected.getEventDate().getMonth(),eventCreated.getEventDate().getMonth());
        Assertions.assertEquals(eventExpected.getEventDate().getYear(),eventCreated.getEventDate().getYear());
        Assertions.assertEquals(eventExpected.getEventDate().getHour(),eventCreated.getEventDate().getHour());
        Assertions.assertEquals(eventExpected.getEventDate().getMinute(),eventCreated.getEventDate().getMinute());
        Assertions.assertEquals(eventExpected.getEventDate().getSecond(),eventCreated.getEventDate().getSecond());
        Assertions.assertEquals(eventExpected.getEventType(),eventCreated.getEventType());
    }

}
