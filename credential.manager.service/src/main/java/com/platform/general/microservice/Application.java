package com.platform.general.microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
//@EnableEurekaClient
@EnableMongoRepositories(basePackages = {"com.platform.general.microservice.web.credential.adapters.mongodb"})
@EnableJpaRepositories(basePackages = {"com.platform.general.microservice.web.credential.adapters.postgresql"})
public class Application {

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
