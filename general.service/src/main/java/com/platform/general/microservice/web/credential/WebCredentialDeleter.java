package com.platform.general.microservice.web.credential;

import com.platform.general.microservice.web.credential.ports.out.WebCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class WebCredentialDeleter {

    @Autowired
    WebCredentialRepository repository;

    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

}
