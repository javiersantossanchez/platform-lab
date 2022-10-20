package com.platform.general.microservice.web.credential.adapters.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuditEventMongodbDao extends MongoRepository<Events, String> {
}
