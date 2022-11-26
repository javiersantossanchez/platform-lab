package com.platform.general.microservice.web.credential;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class WebCredential {
    String password;

    String userName;

    String credentialName;

    LocalDateTime creationDate;

    UUID id;

    UUID userId;
}
