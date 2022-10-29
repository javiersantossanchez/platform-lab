package com.platform.general.microservice.web.credential;

import com.platform.general.microservice.web.credential.exceptions.IllegalArgumentException;
import com.platform.general.microservice.web.credential.ports.in.WebCredentialService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class WebCredentialImpl implements WebCredentialService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebCredentialImpl.class);

    public WebCredentialCreator creator;

    public WebCredentialFetcher fetcher;


    public WebCredentialDeleter deleter;


    public AuditEventRegister eventRegister;

    @Autowired
    public WebCredentialImpl(WebCredentialCreator creator, WebCredentialFetcher fetcher, WebCredentialDeleter deleter, AuditEventRegister eventRegister) {
        this.creator = creator;
        this.fetcher = fetcher;
        this.deleter = deleter;
        this.eventRegister = eventRegister;
    }

    @Override
    public WebCredential createNewWebCredential(String password, String userName, String webSite){
        LOGGER.debug("Starting create a new credential");
        if(StringUtils.isBlank(password)){
            throw new IllegalArgumentException(IllegalArgumentException.Argument.PASSWORD, IllegalArgumentException.Validation.NOT_EMPTY);
        }
        if(StringUtils.isBlank(userName)){
            throw new IllegalArgumentException(IllegalArgumentException.Argument.USER_NAME, IllegalArgumentException.Validation.NOT_EMPTY);
        }
        if(StringUtils.isBlank(webSite)){
            throw new IllegalArgumentException(IllegalArgumentException.Argument.WEB_SITE, IllegalArgumentException.Validation.NOT_EMPTY);
        }
        eventRegister.register(AuditEvent.AuditEventType.CREATE_CREDENTIAL);
        WebCredential credential = creator.create(password, userName, webSite);
        LOGGER.debug("New credential created");
        return credential;
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