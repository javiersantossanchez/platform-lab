package com.platform.general.microservice.web.credential.adapters.postgresql;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "user_password_credential")
@Data
public class WebCredentialEntity {

    @Id
    @GeneratedValue
    @Column(name = "user_password_credential_id",unique = true,nullable = false,updatable = false)
    private UUID id;

    @Column(name = "password",updatable = true,insertable = true,nullable = false)
    private String password;

    @Column(name = "user_name",updatable = true,insertable = true,nullable = false)
    private String userName;

    @Column(name = "credential_name",updatable = true,insertable = true,nullable = false)
    private String credentialName;

    public WebCredentialEntity() {
    }

    public WebCredentialEntity(String password, String userName, String credentialName) {
        this.password = password;
        this.userName = userName;
        this.credentialName = credentialName;
    }
}
