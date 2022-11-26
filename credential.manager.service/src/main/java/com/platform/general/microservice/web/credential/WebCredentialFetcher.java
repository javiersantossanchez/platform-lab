package com.platform.general.microservice.web.credential;

import java.util.UUID;

public interface WebCredentialFetcher {
    WebCredential findById(UUID id);
}
