package com.platform.general.microservice.web.credential.adapters.postgresql;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.platform.general.microservice.web.credential.adapters.postgresql.WebCredentialEntity.UPDATE_PASSWORD;

@Repository
public interface WebCredentialDao extends JpaRepository<WebCredentialEntity, UUID> {
    Optional<WebCredentialEntity> findOneByIdAndUserId(UUID id, UUID userId);

    List<WebCredentialEntity> findByUserId( UUID userId, Pageable pageable);

    @Modifying
    @Transactional
    @Query(name = UPDATE_PASSWORD, nativeQuery = true)
    int updatePassword(String password,UUID credentialId);
}
