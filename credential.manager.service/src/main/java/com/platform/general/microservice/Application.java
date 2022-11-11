package com.platform.general.microservice;

import com.platform.general.microservice.web.credential.adapters.mongodb.AuditEventMongodbDao;
import com.platform.general.microservice.web.credential.adapters.postgresql.WebCredentialDao;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
//import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
//@EnableEurekaClient
@EnableMongoRepositories(basePackages = {"com.platform.general.microservice.web.credential.adapters.mongodb"})
@EnableJpaRepositories(basePackages = {"com.platform.general.microservice.web.credential.adapters.postgresql"})
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
