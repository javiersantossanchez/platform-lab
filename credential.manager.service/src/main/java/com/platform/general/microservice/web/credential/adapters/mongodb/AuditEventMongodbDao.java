package com.platform.general.microservice.web.credential.adapters.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditEventMongodbDao extends MongoRepository<Events, String> {
}
