package com.platform.general.microservice.web.credential;

public interface IWebCredentialCreator {
    WebCredential create(String password, String userName, String webSite);
}
