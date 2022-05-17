package com.platform.general.microservice.web.credential;

import java.time.LocalDateTime;
import java.util.UUID;

public class WebCredential {
    String password;

    String userName;

    String webSite;

    LocalDateTime creationDate;

    LocalDateTime LastUpdateDate;

    UUID id;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "WebCredential{" +
                "password='" + password + '\'' +
                ", userName='" + userName + '\'' +
                ", webSite='" + webSite + '\'' +
                ", id=" + id +
                '}';
    }
}
