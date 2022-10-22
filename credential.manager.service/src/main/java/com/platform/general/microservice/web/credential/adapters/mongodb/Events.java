package com.platform.general.microservice.web.credential.adapters.mongodb;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "events")
@Data
@Builder
public class Events {

    enum EventType{
        SEARCH_WEB_CREDENTIAL,
        CREATE_CREDENTIAL
    }

    @Id
    private String id;

    private String event;

    private EventType eventType;

    private LocalDateTime eventDate;
}
