package com.platform.general.microservice.web.credential;

import java.util.UUID;

public interface WebCredentialFetcher {
    default WebCredential findById(UUID id) {
        return findById(id, UUID.randomUUID());
    }

    WebCredential findById(UUID id, UUID userId);
}
