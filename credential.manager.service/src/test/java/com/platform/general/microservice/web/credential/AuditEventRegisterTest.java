package com.platform.general.microservice.web.credential;

import com.github.javafaker.Faker;
import com.platform.general.microservice.web.credential.exceptions.AuditEventRegistrationException;
import com.platform.general.microservice.web.credential.exceptions.IllegalAuditEventValueException;
import com.platform.general.microservice.web.credential.ports.out.AuditEventRepository;
import com.platform.general.microservice.web.credential.utils.DateManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.ExecutionException;


@ExtendWith(MockitoExtension.class)
public class AuditEventRegisterTest {

    private final Faker faker = new Faker();

    @InjectMocks
    AuditEventRegister target;

    @Mock
    AuditEventRepository auditEventRepository;

    private DateManager dateManager = new DateManager();

    @Test()
    public void createWhenOK() throws ExecutionException, InterruptedException {

        AuditEvent auditEventExpected = AuditEvent.builder()
                .eventDate(dateManager.getCurrentLocalDate())
                .id(faker.internet().uuid())
                .type(AuditEvent.AuditEventType.SEARCH_WEB_CREDENTIAL)
                .build();

        Mockito.doReturn(auditEventExpected).when(auditEventRepository).registerAuditEvent(AuditEvent.AuditEventType.SEARCH_WEB_CREDENTIAL);
        AuditEvent auditEventResult = target.register(AuditEvent.AuditEventType.SEARCH_WEB_CREDENTIAL).get();
        Assertions.assertEquals(auditEventExpected,auditEventResult);
    }

    @Test()
    public void createWhenAnExceptionIsThrow(){

        Mockito.doThrow(new AuditEventRegistrationException()).when(auditEventRepository).registerAuditEvent(AuditEvent.AuditEventType.SEARCH_WEB_CREDENTIAL);
        Assertions.assertThrows(AuditEventRegistrationException.class,()-> target.register(AuditEvent.AuditEventType.SEARCH_WEB_CREDENTIAL));

    }

}
