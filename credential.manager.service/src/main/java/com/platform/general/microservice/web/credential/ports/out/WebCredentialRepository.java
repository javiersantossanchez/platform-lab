package com.platform.general.microservice.web.credential.ports.out;

import com.platform.general.microservice.web.credential.WebCredential;
import com.platform.general.microservice.web.credential.exceptions.IllegalArgumentException;
import com.platform.general.microservice.web.credential.exceptions.*;
import com.platform.general.microservice.web.credential.utils.PagingContext;

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
     * @param userId
     * @exception WebCredentialRegistrationException when an error is generated on saving the credential
     * @return
     */
    WebCredential save(String password, String userName, String webSite, LocalDateTime creationDate, UUID userId);

    /**
     * @return - Credential found on the datastore
     * @throws WebCredentialSearchException   - When a problem is generated on searching
     * @throws IllegalArgumentException       - When {@code credentialId} has invalid value
     * @throws WebCredentialNotFoundException - When The {@code credentialId} does not exist on the system
     * @throws InvalidUserInformationException           - When {@code userId} is null
     * @throws WebCredentialServiceNotAvailableException - when a rate of errors were found on this process.
     */
    WebCredential findById(final UUID credentialId, final UUID userId);

    List<WebCredential> findById(UUID userId, PagingContext paging);

    /**
     * @param id
     * @throws WebCredentialNotFoundException - The id does not exist on the system
     */
    void deleteById(UUID id);
}
