package com.platform.general.microservice.web.credential;

import com.platform.general.microservice.web.credential.exceptions.WebCredentialRegistrationException;

import java.util.UUID;

public interface IWebCredentialCreator {

    /***
     *
     * @param password
     * @param userName
     * @param webSite
     * @param userId
     * @exception WebCredentialRegistrationException when an error is generated on creating the credential
     *
     * @return
     */
    WebCredential create(String password, String userName, String webSite, UUID userId);
}
