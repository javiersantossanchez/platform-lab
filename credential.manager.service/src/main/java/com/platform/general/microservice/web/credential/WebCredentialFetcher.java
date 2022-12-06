package com.platform.general.microservice.web.credential;

import com.platform.general.microservice.web.credential.exceptions.WebCredentialNotFoundException;
import com.platform.general.microservice.web.credential.exceptions.WebCredentialSearchException;

import java.util.UUID;

public interface WebCredentialFetcher {

    /**
     *
     * @param id
     * @param userId
     * @return
     *
     * @exception WebCredentialSearchException - A error its generated during the searching
     * @exception WebCredentialNotFoundException - The id does not exist on the system
     */
    WebCredential findById(UUID id, UUID userId);
}
