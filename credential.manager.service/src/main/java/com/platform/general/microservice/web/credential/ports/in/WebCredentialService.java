package com.platform.general.microservice.web.credential.ports.in;

import com.platform.general.microservice.web.credential.WebCredential;
import com.platform.general.microservice.web.credential.exceptions.WebCredentialNotFoundException;
import com.platform.general.microservice.web.credential.exceptions.WebCredentialSearchException;

import java.util.UUID;

public interface WebCredentialService {

    WebCredential createNewWebCredential(String password, String userName, String webSite, UUID userId);

    /**
     * Search on the system the detail for a credential, based on the id received as parameter
     *
     * @param id     - UUID unique value use as filter for the search.
     * @param userId
     * @return the credential object found in the system with the id
     *
     * @exception WebCredentialSearchException - A error its generated during the searching
     * @exception WebCredentialNotFoundException - The id does not exist on the system
     */
    WebCredential findById(UUID id, UUID userId);

    void deleteByID(UUID id);
}
