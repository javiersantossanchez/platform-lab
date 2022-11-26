package com.platform.general.microservice.web.credential.adapters.postgresql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WebCredentialDao extends JpaRepository<WebCredentialEntity, UUID> {
    Optional<WebCredentialEntity> findOneByIdAndUserId(UUID id, UUID userId);
}
