package com.platform.general.microservice.web.credential.adapters.mongodb;

import com.platform.general.microservice.web.credential.AuditEvent;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "events")
@Data
@Builder
public class Events {

    @Id
    private String id;

    private String event;

    private AuditEvent.AuditEventType eventType;

    private LocalDateTime eventDate;
}
