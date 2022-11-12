package com.platform.general.microservice.web.credential.ports.out;

import com.platform.general.microservice.web.credential.WebCredential;
import com.platform.general.microservice.web.credential.exceptions.IllegalArgumentException;
import com.platform.general.microservice.web.credential.exceptions.WebCredentialRegistrationException;
import com.platform.general.microservice.web.credential.exceptions.WebCredentialSearchException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface WebCredentialRepository {

    /***
     *
     * @param password
     * @param userName
     * @param webSite
     * @param creationDate
     *
     * @exception WebCredentialRegistrationException when an error is generated on saving the credential
     * @return
     */
    WebCredential save(String password, String userName, String webSite, LocalDateTime creationDate);


    /**
     *
     * @exception WebCredentialSearchException - A error its generated during the searching
     * @exception IllegalArgumentException - when @id has invalid value
     * @return
     *
     */
    WebCredential findById(UUID id);
    void deleteById(UUID id);
}
