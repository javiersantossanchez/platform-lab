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
     * @exception WebCredentialSearchException - A error its generated during the searching
     * @exception IllegalArgumentException - when @id has invalid value
     * @exception WebCredentialNotFoundException - The id does not exist on the system
     * @return
     *
     */
    default WebCredential findById(UUID id) {
        return findById(id, null);
    }

    /**
     *
     * @exception WebCredentialSearchException - A error its generated during the searching
     * @exception IllegalArgumentException - when @id has invalid value
     * @exception WebCredentialNotFoundException - The id does not exist on the system
     * @return
     *
     */
    WebCredential findById(UUID credentialId, UUID userId);

    /**
     * @param id
     *
     * @exception WebCredentialNotFoundException - The id does not exist on the system
     */
    void deleteById(UUID id);
}
