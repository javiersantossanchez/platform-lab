package com.platform.general.microservice.web.credential.adapters.inmemory;

import com.platform.general.microservice.web.credential.WebCredential;
import com.platform.general.microservice.web.credential.ports.out.WebCredentialRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component("in-memory")
public class WebCredentialInMemoryRepository implements WebCredentialRepository {

    public static List<WebCredential> webCredentialList = new ArrayList<>();

    @Override
    public WebCredential save(String password, String userName, String webSite, LocalDateTime creationDate, UUID userId) {
        UUID id = UUID.randomUUID();
        WebCredential newCredential = new WebCredential();
        newCredential.setPassword(password);
        newCredential.setUserName(userName);
        newCredential.setCredentialName(webSite);
        newCredential.setCreationDate(creationDate);
        newCredential.setId(id);
        webCredentialList.add(newCredential);
        return newCredential;
    }

    /**
     * @return 
     */
    @Override
    public WebCredential findById(UUID id) {
        return webCredentialList.stream().filter(current -> current.getId().equals(id)).findFirst().get();
    }

    /**
     * @param id
     */
    @Override
    public void deleteById(UUID id) {
        WebCredential credentialToDelete = webCredentialList.stream().filter(current -> current.getId().equals(id)).findFirst().get();
        webCredentialList.remove(credentialToDelete);
    }
}
