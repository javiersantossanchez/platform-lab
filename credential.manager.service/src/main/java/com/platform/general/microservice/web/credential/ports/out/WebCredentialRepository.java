package com.platform.general.microservice.web.credential.ports.out;

import com.platform.general.microservice.web.credential.WebCredential;
import com.platform.general.microservice.web.credential.exceptions.IllegalArgumentException;
import com.platform.general.microservice.web.credential.exceptions.WebCredentialNotFoundException;
import com.platform.general.microservice.web.credential.exceptions.WebCredentialRegistrationException;
import com.platform.general.microservice.web.credential.exceptions.WebCredentialSearchException;

import java.time.LocalDateTime;
import java.util.UUID;

public interface WebCredentialRepository {

    /***
     *
     * @param password
     * @param userName
     * @param webSite
     * @param creationDate
     *
     * @param userId
     * @exception WebCredentialRegistrationException when an error is generated on saving the credential
     * @return
     */
    WebCredential save(String password, String userName, String webSite, LocalDateTime creationDate, UUID userId);

    /**
     *
     * @exception WebCredentialSearchException - When a problem is generated on searching
     * @exception IllegalArgumentException - When {@code credentialId} has invalid value
     * @exception WebCredentialNotFoundException - When The {@code credentialId} does not exist on the system
     * @return - Credential found on the datastore
     *
     */
    WebCredential findById(final UUID credentialId,final UUID userId);

    /**
     * @param id
     *
     * @exception WebCredentialNotFoundException - The id does not exist on the system
     */
    void deleteById(UUID id);
}
