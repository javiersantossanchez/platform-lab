package com.platform.general.microservice.web.credential.adapters.inmemory;

import com.platform.general.microservice.web.credential.WebCredential;
import com.platform.general.microservice.web.credential.ports.out.WebCredentialRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class WebCredentialInMemoryRepository implements WebCredentialRepository {

    public static List<WebCredential> webCredentialList = new ArrayList<>();

    @Override
    public void save() {
        WebCredential newCredential = new WebCredential();
        newCredential.setPassword("jajajajaj");
        newCredential.setUserName("sadadsad");
        webCredentialList.add(newCredential);
    }

    @Override
    public List<WebCredential> findAll() {
        return webCredentialList;
    }

    @Override
    public void deleteAll() {
        webCredentialList.clear();
    }
}
