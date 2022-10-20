package com.platform.general.microservice.web.credential.ports.out;


import com.platform.general.microservice.web.credential.AuditEvent;

public interface AuditEventRepository {

    AuditEvent registerAuditEvent(AuditEvent.AuditEventType type);

}
