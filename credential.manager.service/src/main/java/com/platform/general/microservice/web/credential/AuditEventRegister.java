package com.platform.general.microservice.web.credential;


import com.platform.general.microservice.web.credential.ports.out.AuditEventRepository;
import org.springframework.stereotype.Service;

@Service
public class AuditEventRegister {

    private final AuditEventRepository auditEventRepository;

    public AuditEventRegister(AuditEventRepository auditEventRepository) {
        this.auditEventRepository = auditEventRepository;
    }

    public AuditEvent register(AuditEvent.AuditEventType type){
        return auditEventRepository.registerAuditEvent(type);
    }
}
