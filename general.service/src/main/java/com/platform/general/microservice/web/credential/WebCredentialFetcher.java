package com.platform.general.microservice.web.credential;

import com.platform.general.microservice.web.credential.ports.out.WebCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebCredentialFetcher   {

    @Autowired
    WebCredentialRepository repository;

    public List<WebCredential> fetchAll() {
        return repository.findAll();
    }
/*
    public List<WebCredential> fetchByWebSite(String webSite) {
        return WebCredentialFetcher.credentialList.parallelStream().filter(iem -> iem.getWebSite().equals(webSite)).collect(Collectors.toList());
    }

    public List<WebCredential> fetchById(UUID credentialId) {
        return WebCredentialFetcher.credentialList.parallelStream().filter(iem -> iem.getId().equals(credentialId)).collect(Collectors.toList());
    }
    */
}
