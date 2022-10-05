package com.platform.general.microservice.web.credential;

import com.platform.general.microservice.exceptions.IllegalArgumentException;
import com.platform.general.microservice.web.credential.ports.in.WebCredentialService;
import org.apache.commons.lang3.StringUtils;
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
        if(StringUtils.isBlank(password)){
            throw new IllegalArgumentException(IllegalArgumentException.Argument.PASSWORD, IllegalArgumentException.Validation.NOT_EMPTY);
        }
        if(StringUtils.isBlank(userName)){
            throw new IllegalArgumentException(IllegalArgumentException.Argument.USER_NAME, IllegalArgumentException.Validation.NOT_EMPTY);
        }
        if(StringUtils.isBlank(webSite)){
            throw new IllegalArgumentException(IllegalArgumentException.Argument.WEB_SITE, IllegalArgumentException.Validation.NOT_EMPTY);
        }
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