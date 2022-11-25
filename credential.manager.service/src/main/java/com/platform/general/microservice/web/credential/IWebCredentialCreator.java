package com.platform.general.microservice.web.credential;

import java.util.UUID;

public interface IWebCredentialCreator {
    default WebCredential create(String password, String userName, String webSite) {
        return create(password, userName, webSite,UUID.randomUUID());
    }

    WebCredential create(String password, String userName, String webSite, UUID userId);
}
