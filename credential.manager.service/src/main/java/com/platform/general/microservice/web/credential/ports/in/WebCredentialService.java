package com.platform.general.microservice.web.credential.ports.in;

import com.platform.general.microservice.web.credential.WebCredential;

import java.util.List;
import java.util.UUID;

public interface WebCredentialService {

    WebCredential createNewWebCredential(String password, String userName, String webSite);

    WebCredential findCredential(UUID id);

    List<WebCredential> findAll();

    void deleteByID(UUID id);
}
