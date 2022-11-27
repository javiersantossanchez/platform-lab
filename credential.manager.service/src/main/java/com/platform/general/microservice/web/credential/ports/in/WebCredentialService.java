package com.platform.general.microservice.web.credential.ports.in;

import com.platform.general.microservice.web.credential.WebCredential;

import java.util.UUID;

public interface WebCredentialService {

    WebCredential createNewWebCredential(String password, String userName, String webSite, UUID userId);

    WebCredential findById(UUID id, UUID userId);

    void deleteByID(UUID id);
}
