package com.platform.general.microservice.web.credential.adapters.mongodb;

import com.github.javafaker.Faker;
import com.platform.general.microservice.web.credential.AuditEvent;
import com.platform.general.microservice.web.credential.exceptions.AuditEventRegistrationException;
import com.platform.general.microservice.web.credential.exceptions.IllegalAuditEventValueException;
import com.platform.general.microservice.web.credential.utils.DateManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AuditEventMongodbRepositoryTest {

    private final Faker faker = new Faker();

    private AuditEventMongodbRepository target;

    @Mock
    private AuditEventMongodbDao repo;

    private DateManager dateManager = new DateManager();

    @BeforeEach
    public void setup(){
        target = new AuditEventMongodbRepository(repo,dateManager);
    }

    @Test()
    public void registerAuditEventWithNUllType(){
        IllegalAuditEventValueException exception = Assertions.assertThrows(IllegalAuditEventValueException.class,()->{target.registerAuditEvent(null);});
        Assertions.assertEquals(IllegalAuditEventValueException.Argument.TYPE,exception.getArgument());
        Assertions.assertEquals(IllegalAuditEventValueException.Validation.NOT_EMPTY,exception.getValidationFailed());
    }

    @Test()
    public void registerAuditEventWhenAnExceptionIsThrowByMongodbAccess(){
        Mockito.doThrow(new RuntimeException()).when(repo).save(Mockito.any(Events.class));
        Assertions.assertThrows(AuditEventRegistrationException.class,()->{target.registerAuditEvent(AuditEvent.AuditEventType.SEARCH_WEB_CREDENTIAL);});
    }

    @Test()
    public void registerAuditEventWhenOk(){
        Events dbEvent = Events
                .builder()
                .id(faker.internet().uuid())
                .eventType(AuditEvent.AuditEventType.SEARCH_WEB_CREDENTIAL)
                .eventDate(dateManager.getCurrentLocalDate())
                .build();
        Mockito.doReturn(dbEvent).when(repo).save(Mockito.any(Events.class));
        AuditEvent auditEvent = target.registerAuditEvent(AuditEvent.AuditEventType.SEARCH_WEB_CREDENTIAL);
        Assertions.assertEquals(dbEvent.getId(),auditEvent.getId());
        Assertions.assertEquals(dbEvent.getEventType(),auditEvent.getType());
        Assertions.assertNotNull(auditEvent.getEventDate());
    }

}
