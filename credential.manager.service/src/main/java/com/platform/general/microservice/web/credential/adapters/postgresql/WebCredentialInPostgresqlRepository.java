package com.platform.general.microservice.web.credential.adapters.postgresql;

import com.platform.general.microservice.web.credential.WebCredential;
import com.platform.general.microservice.web.credential.ports.out.WebCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service("postgresql")
public class WebCredentialInPostgresqlRepository implements WebCredentialRepository {

    private WebCredentialDao dao;

    @Autowired
    public WebCredentialInPostgresqlRepository(WebCredentialDao dao) {
        this.dao = dao;
    }

    @Override
    public WebCredential save(String password, String userName, String webSite, LocalDateTime creationDate) {

        WebCredentialEntity entity = new WebCredentialEntity(password,userName,"");
        WebCredentialEntity newEntity =dao.save(entity);

        WebCredential newCredential = new WebCredential();
        newCredential.setPassword(newEntity.getPassword());
        newCredential.setUserName(newEntity.getUserName());
        //newCredential.setWebSite(webSite);
        //newCredential.setCreationDate(creationDate);
        //newCredential.setId(id);
        return newCredential;
    }

    @Override
    public List<WebCredential> findAll() {
        return null;
    }

    /**
     * @return 
     */
    @Override
    public WebCredential findById(UUID id) {
        return null;
    }

    /**
     * @param id
     */
    @Override
    public void deleteById(UUID id) {
    }
}
