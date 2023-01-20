package com.platform.general.microservice.web.credential.worker;

import com.platform.general.microservice.web.credential.WebCredential;
import com.platform.general.microservice.web.credential.exceptions.WebCredentialNotFoundException;
import com.platform.general.microservice.web.credential.exceptions.WebCredentialGeneralException;
import com.platform.general.microservice.web.credential.utils.PagingContext;

import java.util.List;
import java.util.UUID;

public interface WebCredentialFetcher {

    /**
     *
     * @param id
     * @param userId
     * @return
     *
     * @exception WebCredentialGeneralException - A error its generated during the searching
     * @exception WebCredentialNotFoundException - The id does not exist on the system
     */
    WebCredential findById(UUID id, UUID userId);

    List<WebCredential> findByUserId(UUID userId, PagingContext paging);
}
