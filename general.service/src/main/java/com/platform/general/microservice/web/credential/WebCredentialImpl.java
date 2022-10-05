package com.platform.general.microservice.web.credential;

import com.platform.general.microservice.web.credential.ports.in.WebCredentialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class WebCredentialImpl implements WebCredentialService {

    @Autowired
    WebCredentialCreator creator;

    @Autowired
    WebCredentialFetcher fetcher;

    @Autowired
    WebCredentialDeleter deleter;

    @Override
    public WebCredential createNewWebCredential(String password, String userName, String webSite){
        return creator.create(password, userName, webSite);
    }

    /**
     * @param id 
     * @return
     */
    @Override
    public WebCredential findCredential(UUID id) {
        return fetcher.findById(id);
    }

    @Override
    public List<WebCredential> findAll(){
        return fetcher.fetchAll();
    }

    @Override
    public void deleteByID(UUID id){
        deleter.deleteById(id);
    }
}