package com.platform.general.microservice.web.credential.ports.in;

import com.platform.general.microservice.web.credential.WebCredential;

import java.util.UUID;

public interface WebCredentialService {

    default WebCredential createNewWebCredential(String password, String userName, String webSite) {
        return createNewWebCredential(password, userName, webSite, UUID.randomUUID());
    }

    WebCredential createNewWebCredential(String password, String userName, String webSite, UUID userId);

    default WebCredential findById(UUID id) {
        return findById(id, UUID.randomUUID());
    }

    WebCredential findById(UUID id, UUID userId);

    void deleteByID(UUID id);
}
