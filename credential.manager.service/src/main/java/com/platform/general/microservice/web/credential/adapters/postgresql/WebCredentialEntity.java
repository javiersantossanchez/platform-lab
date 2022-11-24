package com.platform.general.microservice.web.credential.adapters.postgresql;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_password_credential")
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
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

    @Column(name = "user_id",updatable = false,insertable = true,nullable = false)
    private UUID userId;

    @Column(name = "creation_time",updatable = false,insertable = true,nullable = false)
    private LocalDateTime  creationTime;


}
