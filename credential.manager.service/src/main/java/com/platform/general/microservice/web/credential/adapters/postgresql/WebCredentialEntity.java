package com.platform.general.microservice.web.credential.adapters.postgresql;


import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "user_password_credential")
@Getter
@Setter
@ToString
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WebCredentialEntity that = (WebCredentialEntity) o;
        return Objects.equals(id, that.id) && password.equals(that.password) && userName.equals(that.userName) && credentialName.equals(that.credentialName) && userId.equals(that.userId) && creationTime.equals(that.creationTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, password, userName, credentialName, userId, creationTime);
    }
}
