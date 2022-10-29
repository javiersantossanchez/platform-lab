package com.platform.general.microservice.web.credential;


import com.platform.general.microservice.web.credential.ports.out.AuditEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class AuditEventRegister {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditEventRegister.class);

    private final AuditEventRepository auditEventRepository;

    public AuditEventRegister(AuditEventRepository auditEventRepository) {
        this.auditEventRepository = auditEventRepository;
    }

    @Async("auditEventThreadPool")
    public CompletableFuture<AuditEvent> register(AuditEvent.AuditEventType type){
        LOGGER.debug("Starting Audit event register {}",type);
        return CompletableFuture.completedFuture(auditEventRepository.registerAuditEvent(type));
    }
}
