package com.platform.general.microservice.web.credential.ports.in;

import com.platform.general.microservice.web.credential.WebCredential;
import com.platform.general.microservice.web.credential.exceptions.WebCredentialNotFoundException;
import com.platform.general.microservice.web.credential.exceptions.WebCredentialSearchException;
import com.platform.general.microservice.web.credential.utils.PagingContext;

import java.util.List;
import java.util.UUID;

public interface WebCredentialUserCases {

    WebCredential createNewWebCredential(String password, String userName, String webSite, UUID userId);

    /**
     * Search on the system the detail for a credential, based on the id received as parameter
     *
     * @param id     - UUID unique value use as filter for the search.
     * @param userId - User id owner of credential
     * @return the credential object found in the system with the id
     *
     * @exception WebCredentialSearchException - A error its generated during the searching
     * @exception WebCredentialNotFoundException - The id does not exist on the system
     */
    WebCredential findById(UUID id, UUID userId);

    List<WebCredential> findByUserId(UUID userId, PagingContext paging);

    void deleteByID(UUID id);
}
