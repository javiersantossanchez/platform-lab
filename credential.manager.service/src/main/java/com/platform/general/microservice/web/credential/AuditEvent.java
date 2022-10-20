package com.platform.general.microservice.web.credential;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AuditEvent {

    public enum AuditEventType{
        SEARCH_WEB_CREDENTIAL
    }

    private AuditEventType type;

    private String id;

    private LocalDateTime eventDate;

}
