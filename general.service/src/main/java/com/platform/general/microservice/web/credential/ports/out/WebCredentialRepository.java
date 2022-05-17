package com.platform.general.microservice.web.credential.ports.out;

import com.platform.general.microservice.web.credential.WebCredential;

import java.util.List;

public interface WebCredentialRepository {

    void save();

    List<WebCredential> findAll();

    void deleteAll();
}
