package com.platform.general.microservice.web.credential.ports.out;

import com.platform.general.microservice.web.credential.WebCredential;

import java.util.List;
import java.util.UUID;

public interface WebCredentialRepository {

    WebCredential save(String password, String userName, String webSite);
    List<WebCredential> findAll();
    WebCredential findById(UUID id);
    void deleteById(UUID id);
}
