package com.platform.general.microservice.web.credential.adapters.postgresql;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class WebCredentialPostgresqlRepositoryResilient {
    private WebCredentialDao dao;

    @Autowired
    public WebCredentialPostgresqlRepositoryResilient(WebCredentialDao dao) {
        this.dao = dao;
    }

    @Retry(name = "retryWebCredentialPostgresqlRepositoryResilient")
    @CircuitBreaker(name = "CircuitBreakerWebCredentialPostgresqlRepositoryResilient")
    public WebCredentialEntity find(final UUID credentialId, final UUID userId) {
        return dao.findOneByIdAndUserId(credentialId, userId).orElse(null);
    }
}
