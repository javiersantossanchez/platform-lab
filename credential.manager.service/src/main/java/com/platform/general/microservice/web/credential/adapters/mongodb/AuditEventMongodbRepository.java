package com.platform.general.microservice.web.credential.adapters.mongodb;

import com.platform.general.microservice.web.credential.AuditEvent;
import com.platform.general.microservice.web.credential.AuditEventRegister;
import com.platform.general.microservice.web.credential.exceptions.AuditEventRegistrationException;
import com.platform.general.microservice.web.credential.exceptions.IllegalAuditEventValueException;
import com.platform.general.microservice.web.credential.ports.out.AuditEventRepository;
import com.platform.general.microservice.web.credential.utils.DateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Repository;


@Repository
public class AuditEventMongodbRepository implements AuditEventRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditEventMongodbRepository.class);

    private final AuditEventMongodbDao repo;

    private final DateManager dateManager;

    @Autowired
    public AuditEventMongodbRepository(AuditEventMongodbDao repo, DateManager dateManager) {
        this.repo = repo;
        this.dateManager = dateManager;
    }


    @Override
    @Retryable(value = { AuditEventRegistrationException.class }, maxAttempts = 3, backoff = @Backoff(delay = 3000))
    public AuditEvent registerAuditEvent(AuditEvent.AuditEventType type) {
        LOGGER.debug("Storing Audit event into mongodb");
        if(type == null){
            throw new IllegalAuditEventValueException(IllegalAuditEventValueException.Argument.TYPE,IllegalAuditEventValueException.Validation.NOT_EMPTY);
        }
        Events eventDb = Events.builder()
                .eventDate(dateManager.getCurrentLocalDate())
                .eventType(type)
                .build();
        try {
            //TODO: Review how the data is stored, there is a conversion and is no storage in UTC. You can review https://chamindu.dev/posts/localdatetime-spring-mongodb/   and https://www.baeldung.com/spring-data-mongodb-zoneddatetime
            eventDb = repo.save(eventDb);
        }catch (Exception exception){
            throw new AuditEventRegistrationException(exception);
        }catch (Throwable t){

        }
        return AuditEvent.builder()
                .eventDate(eventDb.getEventDate())
                .id(eventDb.getId())
                .type(AuditEvent.AuditEventType.valueOf(eventDb.getEventType().name()))
                .build();
    }


}
