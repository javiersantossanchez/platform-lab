package com.platform.general.microservice.web.credential;

import com.platform.general.microservice.web.credential.ports.in.WebCredentialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebCredentialImpl implements WebCredentialService {

    @Autowired
    WebCredentialCreator creator;

    @Autowired
    WebCredentialFetcher fetcher;

    @Autowired
    WebCredentialDeleter deleter;

    @Override
    public void createNewWebCredential(){
        creator.create("","");
    }

    @Override
    public List<WebCredential> findAll(){
        return fetcher.fetchAll();
    }

    @Override
    public void deleteAll(){
        deleter.deleteAll();
    }
}