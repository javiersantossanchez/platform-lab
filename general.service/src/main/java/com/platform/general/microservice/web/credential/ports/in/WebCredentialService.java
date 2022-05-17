package com.platform.general.microservice.web.credential.ports.in;

import com.platform.general.microservice.web.credential.WebCredential;

import java.util.List;

public interface WebCredentialService {

    void createNewWebCredential();

    List<WebCredential> findAll();

    void deleteAll();
}
