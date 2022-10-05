package com.platform.general.microservice.web.credential.adapters.web.dtos;

import java.util.Objects;

public class WebCredentialParam {

    private String password;

    private String userName;

    private String webSite;

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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WebCredentialParam that = (WebCredentialParam) o;
        return Objects.equals(password, that.password) && Objects.equals(userName, that.userName) && Objects.equals(webSite, that.webSite);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password, userName, webSite);
    }
}
